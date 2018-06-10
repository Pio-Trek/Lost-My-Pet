package graded.unit.lostmypetwebapp.service;

import graded.unit.lostmypetwebapp.dao.LostPostDao;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.model.users.User;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer which manages the data of the {@link Lost} pet announcement domain object
 *
 * @author Piotr Przechodzki
 * @since 26/03/2018
 */
@Service
public class LostPostService {

    private final LostPostDao lostPostDao;

    public LostPostService(LostPostDao lostPostDao) {
        this.lostPostDao = lostPostDao;
    }

    /**
     * Fetch a list of lost announcements.
     *
     * @return List of all {@link Lost} object
     */
    public List<Lost> getAllLostPosts() {
        return lostPostDao.fetchAllLostPosts().getBody();
    }

    /**
     * Fetch a list of lost announcements by member id number.
     *
     * @param memberId This is an id number of the {@link User} object by which lost
     *                 announcements will be fetched.
     * @return List of {@link Lost} object
     */
    public List<Lost> getAllLostPostsByMemberId(String memberId) {
        return lostPostDao.fetchAllLostPostsByMemberId(memberId).getBody();
    }

    /**
     * Fetch lost dogs announcements.
     *
     * @return List of {@link Lost} object
     */
    public List<Lost> getAllLostDogsPosts() {
        return lostPostDao.fetchLostDogsPosts().getBody();
    }

    /**
     * Fetch lost cats announcements.
     *
     * @return List of {@link Lost} object
     */
    public List<Lost> getAllLostCatsPosts() {
        return lostPostDao.fetchLostCatsPosts().getBody();
    }

    /**
     * Fetch lost announcement by id.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be fetched.
     * @return {@link Lost} object
     */
    public Lost getLostPostById(Long id) {
        return lostPostDao.fetchLostPostById(id).getBody();
    }

    /**
     * This method will set lost announcement to enabled = true, which will make the post
     * moderated and visible to other users.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be enabled.
     */
    public void setLostPostEnabled(Long id) {
        this.lostPostDao.setLostPostEnabled(id);
    }

    /**
     * Insert a new lost announcement or update the exists if {@link Lost#id} parameter is not empty.
     *
     * @param lost This is the {@link User} member object that will be saved.
     */
    public void saveOrUpdate(Lost lost) {
        this.lostPostDao.insertOrUpdateLostPost(lost);
    }

    /**
     * Delete lost announcement.
     *
     * @param id This is an id number of the {@link Lost} announcement object to be deleted.
     */
    public void deleteLostPostById(Long id) {
        this.lostPostDao.removeLostPost(id);
    }
}
