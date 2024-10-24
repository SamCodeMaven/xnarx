package uz.xnarx.productservice.service.email;

/**
 * @author Samandar Daminov
 * Date: 10/24/2024
 */
public interface EmailSender {
    void send(String to, String email);
}
