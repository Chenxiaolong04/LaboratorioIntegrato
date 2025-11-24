package com.immobiliaris.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.immobiliaris.demo.entity.TipoUtente;
import com.immobiliaris.demo.entity.User;
import com.immobiliaris.demo.repository.UserRepository;
import com.immobiliaris.demo.repository.TipoUtenteRepository;

import java.util.List;
import java.util.regex.Pattern;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TipoUtenteRepository tipoUtenteRepository;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    // Regex per validazioni
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@(.+)$";
    private static final String CF_REGEX = "^[A-Z]{6}\\d{2}[A-Z]\\d{2}[A-Z]\\d{3}[A-Z]$";
    private static final String PHONE_REGEX = "^\\d{10,12}$";
    private static final String CAP_REGEX = "^\\d{5}$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);
    private static final Pattern CF_PATTERN = Pattern.compile(CF_REGEX);
    private static final Pattern PHONE_PATTERN = Pattern.compile(PHONE_REGEX);
    private static final Pattern CAP_PATTERN = Pattern.compile(CAP_REGEX);

    // Metodi di validazione
    private void validateEmail(String email) {
        if(email == null || email.isEmpty()) {
            throw new RuntimeException("Email non può essere vuota");
        }
        if(!EMAIL_PATTERN.matcher(email).matches()) {
            throw new RuntimeException("Formato email non valido");
        }
    }

    private void validatePassword(String password) {
        if(password == null || password.isEmpty()) {
            throw new RuntimeException("Password non può essere vuota");
        }
        if(password.length() < 8) {
            throw new RuntimeException("Password deve essere almeno 8 caratteri");
        }
    }

    private void validateCF(String cf) {
        if(cf != null && !cf.isEmpty()) {
            if(!CF_PATTERN.matcher(cf).matches()) {
                throw new RuntimeException("Codice Fiscale non valido (formato: XXXXXX00X00X000X)");
            }
        }
    }

    private void validatePhone(String phone) {
        if(phone != null && !phone.isEmpty()) {
            if(!PHONE_PATTERN.matcher(phone).matches()) {
                throw new RuntimeException("Telefono non valido (10-12 cifre)");
            }
        }
    }

    private void validateCAP(String cap) {
        if(cap != null && !cap.isEmpty()) {
            if(!CAP_PATTERN.matcher(cap).matches()) {
                throw new RuntimeException("CAP non valido (deve essere 5 cifre)");
            }
        }
    }

    // Registrazione nuovo utente (solo ADMIN)
    public User registerUser(User user, Integer tipoUtenteId) {
        // Validazioni
        validateEmail(user.getEmail());
        validatePassword(user.getPassword());
        validateCF(user.getCf());
        validatePhone(user.getTelefono());
        validateCAP(user.getCap());

        // Controllo email duplicata
        if(userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new RuntimeException("Email già registrata");
        }

        // Hash password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // Data registrazione
        user.setDataRegistrazione(new java.sql.Date(System.currentTimeMillis()));

        // Assegna tipoUtente
        TipoUtente tipo = tipoUtenteRepository.findById(tipoUtenteId)
                .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
        user.setTipoUtente(tipo);

        // Salvataggio
        return userRepository.save(user);
    }

    // Recupera utente per email
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Modifica profilo personale (solo ADMIN)
    public User updateOwnProfile(String email, User updatedUser) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Aggiorna solo i campi forniti con validazioni
        if(updatedUser.getNome() != null && !updatedUser.getNome().isEmpty()) {
            user.setNome(updatedUser.getNome());
        }
        if(updatedUser.getCognome() != null && !updatedUser.getCognome().isEmpty()) {
            user.setCognome(updatedUser.getCognome());
        }
        if(updatedUser.getTelefono() != null && !updatedUser.getTelefono().isEmpty()) {
            validatePhone(updatedUser.getTelefono());
            user.setTelefono(updatedUser.getTelefono());
        }
        if(updatedUser.getCf() != null && !updatedUser.getCf().isEmpty()) {
            validateCF(updatedUser.getCf());
            user.setCf(updatedUser.getCf());
        }
        if(updatedUser.getVia() != null && !updatedUser.getVia().isEmpty()) {
            user.setVia(updatedUser.getVia());
        }
        if(updatedUser.getCitta() != null && !updatedUser.getCitta().isEmpty()) {
            user.setCitta(updatedUser.getCitta());
        }
        if(updatedUser.getCap() != null && !updatedUser.getCap().isEmpty()) {
            validateCAP(updatedUser.getCap());
            user.setCap(updatedUser.getCap());
        }
        // Modifica email solo se diversa e non già in uso
        if(updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && 
           !updatedUser.getEmail().equals(user.getEmail())) {
            validateEmail(updatedUser.getEmail());
            if(userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email già in uso");
            }
            user.setEmail(updatedUser.getEmail());
        }
        // Modifica password se fornita
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            validatePassword(updatedUser.getPassword());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }

        return userRepository.save(user);
    }

    // Modifica utente per ID (solo ADMIN)
    public User updateUserAsAdmin(Long id, User updatedUser) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        // Aggiorna campi personali con validazioni
        if(updatedUser.getNome() != null && !updatedUser.getNome().isEmpty()) {
            user.setNome(updatedUser.getNome());
        }
        if(updatedUser.getCognome() != null && !updatedUser.getCognome().isEmpty()) {
            user.setCognome(updatedUser.getCognome());
        }
        if(updatedUser.getTelefono() != null && !updatedUser.getTelefono().isEmpty()) {
            validatePhone(updatedUser.getTelefono());
            user.setTelefono(updatedUser.getTelefono());
        }
        if(updatedUser.getCf() != null && !updatedUser.getCf().isEmpty()) {
            validateCF(updatedUser.getCf());
            user.setCf(updatedUser.getCf());
        }
        if(updatedUser.getVia() != null && !updatedUser.getVia().isEmpty()) {
            user.setVia(updatedUser.getVia());
        }
        if(updatedUser.getCitta() != null && !updatedUser.getCitta().isEmpty()) {
            user.setCitta(updatedUser.getCitta());
        }
        if(updatedUser.getCap() != null && !updatedUser.getCap().isEmpty()) {
            validateCAP(updatedUser.getCap());
            user.setCap(updatedUser.getCap());
        }
        // Modifica email solo se diversa e non già in uso
        if(updatedUser.getEmail() != null && !updatedUser.getEmail().isEmpty() && 
           !updatedUser.getEmail().equals(user.getEmail())) {
            validateEmail(updatedUser.getEmail());
            if(userRepository.findByEmail(updatedUser.getEmail()).isPresent()) {
                throw new RuntimeException("Email già in uso");
            }
            user.setEmail(updatedUser.getEmail());
        }
        // Modifica password se fornita
        if(updatedUser.getPassword() != null && !updatedUser.getPassword().isEmpty()) {
            validatePassword(updatedUser.getPassword());
            user.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        }
        // SOLO ADMIN può modificare il tipoUtente
        if(updatedUser.getTipoUtente() != null && updatedUser.getTipoUtente().getIdTipo() != null) {
            TipoUtente tipo = tipoUtenteRepository.findById(updatedUser.getTipoUtente().getIdTipo())
                    .orElseThrow(() -> new RuntimeException("Tipo utente non trovato"));
            user.setTipoUtente(tipo);
        }

        return userRepository.save(user);
    }

    // Aggiornamento utente generico (deprecato, usare i metodi specifici)
    public User updateUser(Long id, User updatedUser) {
        return updateUserAsAdmin(id, updatedUser);
    }

    // Lista tutti gli utenti (solo ADMIN)
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Recupero utente per ID (solo ADMIN)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
    }

    // Elimina utente per ID (solo ADMIN)
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));
        userRepository.delete(user);
    }
}
