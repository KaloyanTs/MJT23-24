package bg.sofia.uni.fmi.mjt.passvault.password.checker;

import bg.sofia.uni.fmi.mjt.passvault.password.Password;

public interface PasswordChecker {

    boolean checkPasswordIsCompromised(Password password);
}
