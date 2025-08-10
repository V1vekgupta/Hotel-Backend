package learning.hotelbackend.security.user;

import learning.hotelbackend.model.User;
import learning.hotelbackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Add this import

@Service
@RequiredArgsConstructor
public class HotelUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional // Add this annotation to keep the session open
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Change this line to use the new method
        User user = userRepository.findByEmailWithRoles(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        
        return HotelUserDetails.buildUserDetails(user);
    }
}