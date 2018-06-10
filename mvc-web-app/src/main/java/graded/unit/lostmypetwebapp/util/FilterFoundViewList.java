package graded.unit.lostmypetwebapp.util;

import graded.unit.lostmypetwebapp.model.locations.Location;
import graded.unit.lostmypetwebapp.model.posts.Found;
import graded.unit.lostmypetwebapp.service.FoundPostService;
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
 * This is a helper class used to filter the found announcements list in the view post
 * list and admin dashboard announcements list.
 */
@Service
public class FilterFoundViewList {

    private final FoundPostService foundPostService;
    private List<Found> foundList;

    @Autowired
    public FilterFoundViewList(FoundPostService foundPostService) {
        this.foundPostService = foundPostService;
    }

    /**
     * This is the main process filter method. It is responsible for call all other filter and sort methods.
     *
     * @param type         This parameter describes the pet type by which the list will be filtered.
     * @param locationList This is a list of all locations.
     * @param location     This parameter hold the location by which the list will be filtered.
     * @param sortBy       This parameter hold the information by which date (added or found) the list will be sorted.
     * @param enabled      This parameter hold the information about the announcements moderation preferences by which the list will be filtered.
     * @return Filtered list of {@link Found} objects.
     */
    public List<Found> processFilter(String type, List<Location> locationList, String location, String sortBy, int enabled) {
        this.foundList = filterByPetType(type);
        this.foundList = filterByLocation(foundList, locationList, location);
        this.foundList = sortByDate(foundList, sortBy);
        filterFoundByEnabled(enabled);
        return foundList;
    }

    /**
     * This is the main process filter method. It is responsible for call all other filter methods.
     *
     * @param type         This parameter describes the pet type by which the list will be filtered.
     * @param locationList This is a list of all locations.
     * @param location     This parameter hold the location by which the list will be filtered.
     * @param enabled      This parameter hold the information about the announcements moderation preferences by which the list will be filtered.
     * @return Filtered list of {@link Found} objects.
     */
    public List<Found> processFilter(String type, List<Location> locationList, String location, int enabled) {
        this.foundList = filterByPetType(type);
        this.foundList = filterByLocation(foundList, locationList, location);
        filterFoundByEnabled(enabled);
        return foundList;
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
     * @return Filtered list of {@link Found} objects.
     */
    public List<Found> processFilter(Date start, Date end, String type, List<Location> locationList, String location, int enabled) {
        this.foundList = filterByPetType(type);
        if (start != null && end != null) {
            this.foundList = filterDatesBetweenStartAndFinishOrEqual(start, end);
        }
        this.foundList = filterByLocation(foundList, locationList, location);
        filterFoundByEnabled(enabled);
        return foundList;
    }

    /**
     * Filter the list of found announcements by start and end time.
     *
     * @param start This is the start or from what date we want to filter the announcements.
     * @param end   This is the end or to what date we want to filter the announcements.
     * @return Filtered list of {@link Found} objects.
     */
    private List<Found> filterDatesBetweenStartAndFinishOrEqual(Date start, Date end) {
        return this.foundList.stream()
                .filter(d -> (d.getAddedDate().after(start) && d.getAddedDate().before(end)))
                .collect(Collectors.toList());
    }

    /**
     * Filter the list by pet type.
     */
    private List<Found> filterByPetType(String type) {
        if (equalsIgnoreCase(type, "dogs")) {
            this.foundList = foundPostService.getAllFoundDogsPosts();
        } else if (equalsIgnoreCase(type, "cats")) {
            this.foundList = foundPostService.getAllFoundCatsPosts();
        } else {
            this.foundList = foundPostService.getAllFoundPosts();
        }
        return foundList;
    }

    /**
     * Filter the list by location.
     */
    private List<Found> filterByLocation(List<Found> foundList, List<Location> locationList, String location) {
        if (isNotEmpty(location)) {
            boolean locationExists = locationList.stream()
                    .anyMatch(l -> equalsIgnoreCase(l.getName(), location));

            if (locationExists) {
                foundList = foundList.stream()
                        .filter(l -> equalsIgnoreCase(l.getLocation().getName(), location))
                        .collect(toList());
            }
        }
        return foundList;
    }

    /**
     * Sort the list by added or found date.
     */
    private List<Found> sortByDate(List<Found> foundList, String sortBy) {
        if (equalsIgnoreCase(sortBy, "foundDate")) {
            foundList.sort(comparing(Found::getFoundDate));
        } else {
            foundList.sort(comparing(Found::getAddedDate));
        }

        return foundList;
    }


    /**
     * Filter the list by enabled (moderation) parameter.
     */
    private void filterFoundByEnabled(int enabled) {
        switch (enabled) {
            case 0:
                this.foundList = foundList.stream().filter(f -> f.getEnabled().equals(false)).collect(Collectors.toList());
                break;
            case 1:
                this.foundList = foundList.stream().filter(f -> f.getEnabled().equals(true)).collect(Collectors.toList());
                break;
            default:
                break;
        }
    }
}
