package uz.pdp.rentseekerlongpolling.service;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import uz.pdp.rentseekerlongpolling.entity.Home;
import uz.pdp.rentseekerlongpolling.entity.Search;
import uz.pdp.rentseekerlongpolling.entity.User;
import uz.pdp.rentseekerlongpolling.payload.SearchDTO;
import uz.pdp.rentseekerlongpolling.repository.HomeRepository;
import uz.pdp.rentseekerlongpolling.repository.SearchRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SearchService {


    private final HomeService homeService;

    private final SearchRepository searchRepository;

    private final ModelMapper modelMapper;


    public Search getSearchByUserId(User user) {
        return searchRepository.findByUserId(user.getId()).orElseGet(() -> searchRepository.save(new Search(user)));
    }

    public void addSearch(Search search) {
        searchRepository.save(search);
    }

    public List<Home> searchHome(Search search) {
        return homeService.searchHomes(modelMapper.map(search,SearchDTO.class));
    }


}
