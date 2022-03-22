package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Interest;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.repository.InterestRepository;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InterestService {


    private final HomeService homeService;

    private final InterestRepository interestRepository;

    public boolean changeVisible(Home home, User user) {
        Interest interest;
        Optional<Interest> optionalInterest = interestRepository.findByHomeIdAndUserId(home.getId(), user.getId());
        if(optionalInterest.isPresent()){
             interest = optionalInterest.get();
             interest.setVisible(!interest.isVisible());
             interestRepository.save(interest);
             return interest.isVisible();
        }
        interestRepository.save(new Interest(home,user,true));
        if(!home.getUser().getChatId().equals(user.getChatId()))
        homeService.changeCountOfInterest(home);
        return true;
    }

    public boolean getVisible(Home home, User user) {
        Optional<Interest> optional = interestRepository.findByHomeIdAndUserId(home.getId(), user.getId());
        return optional.isPresent() && optional.get().isVisible();
    }

}
