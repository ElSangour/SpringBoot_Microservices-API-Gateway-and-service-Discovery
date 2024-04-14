package com.example.moviecatalogservice.resources;

import com.example.moviecatalogservice.models.CatalogItem;
import com.example.moviecatalogservice.models.Movie;
import com.example.moviecatalogservice.models.Rating;
import com.example.moviecatalogservice.models.UserRating;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/catalog")

public class MovieCatalogResource {


    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private WebClient.Builder webClientBuilder ;
    @RequestMapping("/{userId}")
        public List <CatalogItem> getCatalog(@PathVariable("userId") String userId ){
        //get all movies IDs
        // RestTemplate restTemplate=new RestTemplate();


        UserRating ratings = restTemplate.getForObject("https://localhost:8082/ratingsdata/users"+ userId, UserRating.class);
        //for each movie ID , call movie info service w neko men ando details
        return ratings.getUserRating().stream().map(rating-> {
        Movie movie=restTemplate.getForObject("http://localhost:8081/movies/"+rating.getMovieId(), Movie.class);
                    //put them all together
                    return new CatalogItem(movie.getName(),"DESC",rating.getRating());
        })
                .collect(Collectors.toList());


    }
}
//            Movie movie = webClientBuilder.build()
//                    .get()
//                    .uri("http://localhost:8081/movies/"+rating.getRating())
//                    .retrieve()
//                    .bodyToMono(Movie.class)
//                    .block();
