package com.example.ex6.service;

import com.example.ex6.dto.MovieDTO;
import com.example.ex6.dto.PageRequestDTO;
import com.example.ex6.dto.PageResultDTO;
import com.example.ex6.entity.Movie;
import com.example.ex6.entity.MovieImage;
import com.example.ex6.repository.MovieImageRepository;
import com.example.ex6.repository.MovieRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {
  private final MovieRepository movieRepository;
  private final MovieImageRepository movieImageRepository;

  @Override
  public PageResultDTO<MovieDTO, Object[]> getList(PageRequestDTO pageRequestDTO) {
    // 검색하기 위한 객체
    Pageable pageable = pageRequestDTO.getPageable(Sort.by("mno").descending());

    // 1. Page
    //Page<Object[]> result = movieRepository.getListPageMaxMi(pageable);
    Page<Object[]> result = movieRepository.searchPage(
        pageRequestDTO.getType(), pageRequestDTO.getKeyword(), pageable);

    // 2. Function
    Function<Object[], MovieDTO> function = arr -> entitiesToDTO(
        (Movie) arr[0], (List<MovieImage>) (Arrays.asList((MovieImage) arr[1])),
        (Double) arr[2], (Long) arr[3]
    );
    return new PageResultDTO<>(result, function);
  }

  @Override
  public Long register(MovieDTO movieDTO) {
    Map<String, Object> map = dtoToEntity(movieDTO);
    Movie movie = (Movie)map.get("movie");
    List<MovieImage> movieImageList =(List<MovieImage>) map.get("imgList");
    movieRepository.save(movie);
    movieImageList.forEach(image -> movieImageRepository.save(image));
    return movie.getMno();
  }

  @Override
  public MovieDTO get(Long mno) {
    List<Object[]> result = movieRepository.getMovieWithAll(mno);
    //m.avg,reviewCnt 안바뀜, mi 바뀜
    Movie movie = (Movie)result.get(0)[0];
    List<MovieImage> movieImageList = new ArrayList<>();
    result.forEach(new Consumer<Object[]>() {
      @Override
      public void accept(Object[] row) {
        MovieImage movieImage = (MovieImage) row[1];
        movieImageList.add(movieImage);
      }
    });
    Double avg = (Double)result.get(0)[2];
    Long reviewCnt =  (Long)result.get(0)[3];
    return null;
  }

  @Override
  public void modify(MovieDTO movieDTO) {

  }

  @Override
  public void removeWithReviewsAndMovieImages(Long mno) {

  }

  @Override
  public void removeMovieImagebyUUID(String uuid) {

  }
}