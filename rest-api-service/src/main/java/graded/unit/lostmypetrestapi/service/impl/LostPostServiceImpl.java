package graded.unit.lostmypetrestapi.service.impl;

import graded.unit.lostmypetrestapi.entity.posts.Lost;
import graded.unit.lostmypetrestapi.repository.LostPostRepository;
import graded.unit.lostmypetrestapi.service.LostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of {@link LostPostService} interface
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class LostPostServiceImpl implements LostPostService {

    private final LostPostRepository repository;

    @Autowired
    public LostPostServiceImpl(LostPostRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Lost> getAllLostPosts() {
        return repository.findAll();
    }

    @Override
    public List<Lost> getAllLostPostsByMemberId(String memberId) {
        return repository.findAllByMemberId(memberId);
    }

    @Override
    public Optional<Lost> getLostPostById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ResponseEntity<Lost> addLostPost(Lost lost) {
        // Set current date and time for the announcement
        lost.setAddedDate(new Date());
        return new ResponseEntity<>(repository.save(lost), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<Lost> updateLostPost(Lost lost, Date addedDate) {
        // Set to the updated object date and time of the current lost announcement
        lost.setAddedDate(addedDate);
        return new ResponseEntity<>(repository.save(lost), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> deleteLostPost(Long id) {
        this.repository.deleteById(id);
        return new ResponseEntity<>("Lost announcement deleted. (ID: " + id + ")", HttpStatus.OK);
    }

    @Override
    public int countByEnabledFalse() {
        return repository.countByEnabledFalse();
    }
}
