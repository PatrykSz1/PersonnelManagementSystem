package pl.szymanczyk.peoplemanagement.configuartion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import pl.szymanczyk.peoplemanagement.repository.PersonRepository;

@Component
public class PersonInfoDetailService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("user not found"));
    }
}
