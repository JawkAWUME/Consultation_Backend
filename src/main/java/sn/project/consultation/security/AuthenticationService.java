package sn.project.consultation.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sn.project.consultation.api.dto.AuthenticationRequest;
import sn.project.consultation.api.dto.AuthenticationResponse;
import sn.project.consultation.api.dto.RegisterRequest;
import sn.project.consultation.data.entities.Coordonnees;
import sn.project.consultation.data.entities.Patient;
import sn.project.consultation.data.entities.ProSante;
import sn.project.consultation.data.entities.User;
import sn.project.consultation.data.enums.RoleUser;
import sn.project.consultation.data.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest request) {
        User user;

        switch (request.getRole()) {
            case PATIENT:
                Patient patient = new Patient();
                Coordonnees coordonnees = new Coordonnees();
                coordonnees.setEmail(request.getEmail());
                patient.setCoordonnees(coordonnees);
                patient.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
                patient.setRole(RoleUser.PATIENT);
                user = patient;
                break;
            case PROFESSIONNEL_SANTE:
                ProSante pro = new ProSante();
                coordonnees = new Coordonnees();
                coordonnees.setEmail(request.getEmail());
                pro.setCoordonnees(coordonnees);
                pro.setMotDePasse(passwordEncoder.encode(request.getMotDePasse()));
                pro.setRole(RoleUser.PROFESSIONNEL_SANTE);
                user = pro;
                break;
            default:
                throw new IllegalArgumentException("Rôle non pris en charge : " + request.getRole());
        }

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user); // User est de type UserDetails
        return new AuthenticationResponse(jwtToken);
    }
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getMotDePasse())
        );
        User user = userRepository.findByEmail(request.getEmail());
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationResponse(jwtToken);
    }
}
