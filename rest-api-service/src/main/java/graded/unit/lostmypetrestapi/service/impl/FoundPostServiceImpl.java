package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.posts.Found;
import graded.unit.lostmypetrestapi.repository.FoundPostRepository;
import graded.unit.lostmypetrestapi.service.FoundPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link FoundPostService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class FoundPostServiceImpl implements FoundPostService {

    private final FoundPostRepository repository;

    @Autowired
    public FoundPostServiceImpl(FoundPostRepository repository) {
        this.repository = repository;
    }


    @Override
    public List<Found> getAllFoundPosts() {
        return repository.findAll();
    }

    @Override
    public List<Found> getAllFoundPostsByMemberId(String memberId) {
        return repository.findAllByMemberId(memberId);
    }

    @Override
    public Optional<Found> getFoundPostById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<Found> addFoundPost(Found found) {
        // Set current date and time for the announcement
        found.setAddedDate(new Date());
        return new ResponseEntity<>(repository.save(found), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Found> updateFoundPost(Found found, Date addedDate) {
        // Set to the updated object date and time of the current lost announcement
        found.setAddedDate(addedDate);
        return new ResponseEntity<>(repository.save(found), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteFoundPost(Long id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Found announcement deleted. (ID: " + id + ")", HttpStatus.OK);
    }

    @Override
    public int countByEnabledFalse() {
        return repository.countByEnabledFalse();
    }
}
