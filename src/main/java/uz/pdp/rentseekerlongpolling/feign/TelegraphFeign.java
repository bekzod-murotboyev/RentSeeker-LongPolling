package uz.pdp.rentseekerlongpolling.feign;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.telegram.telegraph.api.objects.Page;
import uz.pdp.rentseekerlongpolling.payload.telegram.telegraph.CreatedPageDTO;

import static uz.pdp.rentseekerlongpolling.util.security.Telegraph.BASE;
import static uz.pdp.rentseekerlongpolling.util.security.Telegraph.CREATE_PAGE;

@FeignClient(url = BASE,name = "TelegraphFeign")
public interface TelegraphFeign {

    @GetMapping(CREATE_PAGE)
    CreatedPageDTO createPage(@RequestParam("access_token") String accessToken,
                              @RequestParam String title,
                              @RequestParam String content);

    @GetMapping(CREATE_PAGE)
    CreatedPageDTO createPage(@RequestParam("access_token") String accessToken,
                              @RequestParam String title,
                              @RequestParam(value = "author_name",required = false) String authorName,
                              @RequestParam(value = "author_url",required = false) String authorUrl,
                              @RequestParam(value = "return_content",required = false) Boolean returnContent,
                              @RequestParam String content);



    @GetMapping(CREATE_PAGE)
    Page editPage(@RequestParam("access_token") String accessToken,
                  @RequestParam String path,
                  @RequestParam String title,
                  @RequestParam(value = "author_name",required = false) String authorName,
                  @RequestParam(value = "author_url",required = false) String authorUrl,
                  @RequestParam String content,
                  @RequestParam(value = "return_content",required = false) Boolean returnContent);

}
