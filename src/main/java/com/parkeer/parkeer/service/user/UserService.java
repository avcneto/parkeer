package com.parkeer.parkeer.service.user;

import com.parkeer.parkeer.dto.user.UserDTO;
import com.parkeer.parkeer.dto.user.UserUpdateDTO;
import com.parkeer.parkeer.entity.user.User;
import com.parkeer.parkeer.exception.FailedDependencyException;
import com.parkeer.parkeer.exception.ResourceAlreadyExistsException;
import com.parkeer.parkeer.repository.user.UserRepository;
import com.parkeer.parkeer.util.QueryParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import reactor.core.publisher.Mono;

import java.lang.reflect.Field;

import static com.parkeer.parkeer.config.SecurityConfig.passwordEncoder;
import static com.parkeer.parkeer.util.Validators.isNullOrBlank;

@Slf4j
@Service
public class UserService {

    private static final String EMAIL_OR_CPF_REGISTERED = "Email or CPF already registered";
    private static final String NON_MATCHING = "non-matching fields between UserUpdateDTO and User";
    private static final String INACCESSIBLE_FIELDS = "user update has inaccessible fields";
    private static final String PASSWORD = "password";
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> createUser(final UserDTO userDTO) {
        return Mono
                .just(new User(userDTO))
                .flatMap(user ->
                        userRepository.existsByEmailOrCpf(user.getEmail(), user.getCpf())
                                .flatMap(exist -> {
                                    if (exist) {
                                        return Mono.error(new ResourceAlreadyExistsException(EMAIL_OR_CPF_REGISTERED));
                                    }

                                    return Mono.just(user);
                                })
                                .onErrorResume(ResourceAlreadyExistsException.class, Mono::error)
                                .flatMap(userRepository::save)
                );
    }

    public Mono<User> getUserByCpfOrEmailOrId(final MultiValueMap<String, String> params) {
        return Mono
                .just(params)
                .flatMap(it -> {
                    var query = new QueryParams(it);
                    query.validateIdAndCpfAndEmail();

                    return userRepository.findByCpfOrEmailOrId(query.getCpf(), query.getEmail(), query.getIdOrNull());
                });
    }

    public Mono<User> updateUserById(final MultiValueMap<String, String> params, final UserUpdateDTO userUpdateDTO) {
        return Mono
                .just(userUpdateDTO)
                .flatMap(userUpdate -> userRepository
                        .findById(new QueryParams(params).getIdOrBadRequestException())
                        .flatMap(user -> {
                            updateUserByUserUpdateDTO(userUpdate, user);
                            return userRepository.save(user);
                        })
                );

    }

    public Mono<Void> deleteUserById(final MultiValueMap<String, String> params) {
        return Mono
                .just(params)
                .flatMap(it -> userRepository.deleteById(new QueryParams(it).getIdOrBadRequestException()));
    }


    private void updateUserByUserUpdateDTO(final UserUpdateDTO userUpdateDTO, User user) {
        Field[] fields = UserUpdateDTO.class.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(userUpdateDTO);
            } catch (IllegalAccessException ex) {
                throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
            }

            if (value != null && !isNullOrBlank(value.toString())) {
                Field correspondingField;

                try {
                    correspondingField = User.class.getDeclaredField(field.getName());
                    correspondingField.setAccessible(true);

                    if (PASSWORD.equalsIgnoreCase(field.getName())) {
                        correspondingField.set(user, passwordEncoder().encode(value.toString()));
                    } else {
                        correspondingField.set(user, value);
                    }

                } catch (NoSuchFieldException ex) {
                    throw new FailedDependencyException(NON_MATCHING, ex);
                } catch (IllegalAccessException ex) {
                    throw new FailedDependencyException(INACCESSIBLE_FIELDS, ex);
                }
            }
        }

    }
}
