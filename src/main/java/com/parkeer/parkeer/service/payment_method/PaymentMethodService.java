package com.parkeer.parkeer.service.payment_method;

import com.parkeer.parkeer.dto.payment_method.PaymentMethodDTO;
import com.parkeer.parkeer.dto.payment_method.PaymentUpdateMethodDTO;
import com.parkeer.parkeer.entity.payment_method.PaymentMethod;
import com.parkeer.parkeer.exception.FailedDependencyException;
import com.parkeer.parkeer.exception.NotFoundException;
import com.parkeer.parkeer.exception.ResourceAlreadyExistsException;
import com.parkeer.parkeer.repository.payment_method.PaymentMethodRepository;
import com.parkeer.parkeer.repository.user.UserRepository;
import com.parkeer.parkeer.util.QueryParams;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static com.parkeer.parkeer.util.Validators.isNullOrBlank;

@Service
public class PaymentMethodService {

    private static final String INACCESSIBLE_FIELDS = "user update has inaccessible fields";
    private static final String NON_MATCHING = "non-matching fields between UserUpdateDTO and User";
    private static final String UNREGISTERED_USER = "unregistered user";
    private static final String USER_ALREADY_HAS_PAYMENT_METHOD_REGISTERED = "user has payment method registered";

    private final UserRepository userRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodService(UserRepository userRepository, PaymentMethodRepository paymentMethodRepository) {
        this.userRepository = userRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public Mono<PaymentMethod> addPaymentMethod(final PaymentMethodDTO paymentMethodDTO) {
        return userRepository.findById(paymentMethodDTO.userId())
                .switchIfEmpty(Mono.error(new NotFoundException(UNREGISTERED_USER)))
                .flatMap(user -> paymentMethodRepository.findByUserId(user.getId())
                        .hasElement()
                        .flatMap(existingPaymentMethod -> {
                            if (existingPaymentMethod) {
                                return Mono.error(new ResourceAlreadyExistsException(USER_ALREADY_HAS_PAYMENT_METHOD_REGISTERED));
                            } else {
                                return paymentMethodRepository.save(new PaymentMethod(paymentMethodDTO));
                            }
                        }));
    }

    public Mono<Void> deletePaymentMethodById(final MultiValueMap<String, String> params) {
        return Mono.just(params)
                .flatMap(it -> paymentMethodRepository.deleteById(new QueryParams(it).getIdOrBadRequestException()));
    }

    public Mono<PaymentMethod> updateVehicleById(final MultiValueMap<String, String> params, final PaymentUpdateMethodDTO paymentUpdateMethodDTO) {
        return Mono
                .just(paymentUpdateMethodDTO)
                .flatMap(paymentUpdateDTO -> paymentMethodRepository
                        .findById(new QueryParams(params).getIdOrBadRequestException())
                        .flatMap(paymentMethod -> {

                            updatePaymentMethodByPaymentMethodUpdateDTO(paymentUpdateDTO, paymentMethod);
                            return paymentMethodRepository.save(paymentMethod);
                        })
                );

    }

    public Flux<PaymentMethod> getAllPaymentMethodOrById(final MultiValueMap<String, String> params) {
        return Flux
                .just(params)
                .flatMap(it -> {
                    var query = new QueryParams(it);
                    var id = query.getIdOrNull();

                    if (id == null) {
                        return paymentMethodRepository.findAll();
                    }

                    return paymentMethodRepository.findById(id);
                });
    }


    private void updatePaymentMethodByPaymentMethodUpdateDTO(final PaymentUpdateMethodDTO paymentUpdateMethodDTO, PaymentMethod paymentMethod) {
        Field[] fields = PaymentUpdateMethodDTO.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(paymentUpdateMethodDTO);
            } catch (IllegalAccessException ex) {
                throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
            }

            if (value != null && !isNullOrBlank(value.toString())) {
                Field correspondingField;

                try {
                    correspondingField = PaymentMethod.class.getDeclaredField(field.getName());
                    correspondingField.setAccessible(true);

                    correspondingField.set(paymentMethod, value);

                } catch (NoSuchFieldException ex) {
                    throw new FailedDependencyException(NON_MATCHING, ex);
                } catch (IllegalAccessException ex) {
                    throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
                }
            }
        }

    }
}
