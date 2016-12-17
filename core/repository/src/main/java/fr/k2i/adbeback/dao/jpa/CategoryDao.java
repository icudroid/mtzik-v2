package fr.k2i.adbeback.dao.jpa;


import fr.k2i.adbeback.core.business.media.Category;
import fr.k2i.adbeback.dao.ICategoryDao;
import org.springframework.stereotype.Repository;

/**
 * User: dimitri
 * Date: 29/01/14
 * Time: 14:03
 * Goal:
 */
@Repository
public class CategoryDao extends GenericDaoJpa<Category, Long> implements ICategoryDao {
}
