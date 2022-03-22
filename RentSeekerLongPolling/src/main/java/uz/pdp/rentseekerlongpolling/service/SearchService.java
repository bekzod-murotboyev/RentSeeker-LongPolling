package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Search;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {


    private final HomeService homeService;

    private final SearchRepository searchRepository;


    public Search getSearchByUserId(User user) {
        return searchRepository.findByUserId(user.getId()).orElseGet(()->searchRepository.save(new Search(user)));
    }

    public void addSearch(Search search) {
        searchRepository.save(search);
    }

    public List<Home> searchHome(Search search) {
        List<Home> found = new ArrayList<>();
        for (Home home : homeService.getAllHome()) {
            if (search.getRegion() != null)
                if (search.getRegion().equals(home.getRegion())) {
                    if (search.getDistrict() != null)
                        if (!search.getDistrict().equals(home.getDistrict()))
                            continue;
                } else continue;

            if (search.getHomeType() != null)
                if (!home.getHomeType().equals(search.getHomeType()))
                    continue;
            if (search.getStatus() != null)
                if (!search.getStatus().equals(home.getStatus()))
                    continue;
            if (search.getNumberOfRooms() != -1)
                if (search.getNumberOfRooms() != home.getNumberOfRooms())
                    continue;
            if (search.getMinPrice() != -1)
                if (search.getMinPrice() > home.getPrice())
                    continue;
            if (search.getMaxPrice() != -1)
                if (search.getMaxPrice() < home.getPrice())
                    continue;
            found.add(home);
        }
        return found;
    }


}
