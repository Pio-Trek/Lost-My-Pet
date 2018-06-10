package graded.unit.lostmypetwebapp.util;

import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.posts.Lost;
import graded.unit.lostmypetwebapp.service.LostPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.equalsIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * This is a helper class used to filter the lost announcements list in the view post
 * list and admin dashboard announcements list.
 */
@Service
public class FilterLostViewList {

    private final LostPostService lostPostService;
    private List<Lost> lostList;

    @Autowired
    public FilterLostViewList(LostPostService lostPostService) {
        this.lostPostService = lostPostService;
    }

    /**
     * This is the main process filter method. It is responsible for call all other filter and sort methods.
     *
     * @param type         This parameter describes the pet type by which the list will be filtered.
     * @param locationList This is a list of all locations.
     * @param location     This parameter hold the location by which the list will be filtered.
     * @param sortBy       This parameter hold the information by which date (added or found) the list will be sorted.
     * @param enabled      This parameter hold the information about the announcements moderation preferences by which the list will be filtered.
     * @return Filtered list of {@link Lost} objects.
     */
    public List<Lost> processFilter(String type, List<Location> locationList, String location, String sortBy, int enabled) {
        this.lostList = filterByPetType(type);
        this.lostList = filterByLocation(lostList, locationList, location);
        this.lostList = sortByDate(lostList, sortBy);
        filterLostByEnabled(enabled);
        return lostList;
    }

    /**
     * This is the main process filter method. It is responsible for call all other filter methods.
     *
     * @param type         This parameter describes the pet type by which the list will be filtered.
     * @param locationList This is a list of all locations.
     * @param location     This parameter hold the location by which the list will be filtered.
     * @param enabled      This parameter hold the information about the announcements moderation preferences by which the list will be filtered.
     * @return Filtered list of {@link Lost} objects.
     */
    public List<Lost> processFilter(String type, List<Location> locationList, String location, int enabled) {
        this.lostList = filterByPetType(type);
        this.lostList = filterByLocation(lostList, locationList, location);
        filterLostByEnabled(enabled);
        return lostList;
    }

    /**
     * This is the main process filter method. It is responsible for call all other filtered method and filter the
     * generated document from and to selected date.
     *
     * @param start        This is the start or from what date we want to filter the announcements.
     * @param end          This is the end or to what date we want to filter the announcements.
     * @param type         This parameter describes the pet type by which the list will be filtered.
     * @param locationList This is a list of all locations.
     * @param location     This parameter hold the location by which the list will be filtered.
     * @param enabled      This parameter hold the information about the announcements moderation preferences by which the list will be filtered.
     * @return Filtered list of {@link Lost} objects.
     */
    public List<Lost> processFilter(Date start, Date end, String type, List<Location> locationList, String location, int enabled) {
        this.lostList = filterByPetType(type);
        if (start != null && end != null) {
            this.lostList = filterDatesBetweenStartAndFinishOrEqual(start, end);
        }
        this.lostList = filterByLocation(lostList, locationList, location);
        filterLostByEnabled(enabled);
        return lostList;
    }

    /**
     * Filter the list of lost announcements by start and end time.
     *
     * @param start This is the start or from what date we want to filter the announcements.
     * @param end   This is the end or to what date we want to filter the announcements.
     * @return Filtered list of {@link Lost} objects.
     */
    private List<Lost> filterDatesBetweenStartAndFinishOrEqual(Date start, Date end) {
        return this.lostList.stream()
                .filter(d -> (d.getAddedDate().after(start) && d.getAddedDate().before(end)))
                .collect(Collectors.toList());
    }

    /**
     * Filter the list by pet type.
     */
    private List<Lost> filterByPetType(String type) {
        if (equalsIgnoreCase(type, "dogs")) {
            this.lostList = lostPostService.getAllLostDogsPosts();
        } else if (equalsIgnoreCase(type, "cats")) {
            this.lostList = lostPostService.getAllLostCatsPosts();
        } else {
            this.lostList = lostPostService.getAllLostPosts();
        }
        return lostList;
    }

    /**
     * Filter the list by location.
     */
    private List<Lost> filterByLocation(List<Lost> lostList, List<Location> locationList, String location) {
        if (isNotEmpty(location)) {
            boolean locationExists = locationList.stream()
                    .anyMatch(l -> equalsIgnoreCase(l.getName(), location));

            if (locationExists) {
                lostList = lostList.stream()
                        .filter(l -> equalsIgnoreCase(l.getLocation().getName(), location))
                        .collect(toList());
            }
        }
        return lostList;
    }

    /**
     * Sort the list by added or found date.
     */
    private List<Lost> sortByDate(List<Lost> lostList, String sortBy) {
        if (equalsIgnoreCase(sortBy, "lostDate")) {
            lostList.sort(comparing(Lost::getLostDate));
        } else {
            lostList.sort(comparing(Lost::getAddedDate));
        }
        return lostList;
    }

    /**
     * Filter the list by enabled (moderation) parameter.
     */
    private void filterLostByEnabled(int enabled) {
        switch (enabled) {
            case 0:
                this.lostList = lostList.stream().filter(f -> f.getEnabled().equals(false)).collect(Collectors.toList());
                break;
            case 1:
                this.lostList = lostList.stream().filter(f -> f.getEnabled().equals(true)).collect(Collectors.toList());
                break;
            default:
                break;
        }
    }
}