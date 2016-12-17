package fr.k2i.adbeback.dao.jpa;


import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import fr.k2i.adbeback.dao.IGenericDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.mapping.PropertyPath;
import org.springframework.data.querydsl.QSort;
import org.springframework.data.querydsl.SimpleEntityPathResolver;
import org.springframework.util.Assert;
import com.mysema.query.jpa.EclipseLinkTemplates;
import com.mysema.query.jpa.HQLTemplates;
import com.mysema.query.jpa.JPQLQuery;
import com.mysema.query.jpa.OpenJPATemplates;
import com.mysema.query.jpa.impl.AbstractJPAQuery;
import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.support.Expressions;
import com.mysema.query.types.EntityPath;
import com.mysema.query.types.Expression;
import com.mysema.query.types.OrderSpecifier;
import com.mysema.query.types.OrderSpecifier.NullHandling;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathBuilder;

/**
 * This class serves as the Base class for all other DAOs - namely to hold
 * common CRUD methods that they might all use. You should only need to extend
 * this class when your require custom CRUD logic.
 * <p/>
 * <p>To register this class in your Spring context file, use the following XML.
 * <pre>
 *      &lt;bean id="fooDao" class="org.appfuse.dao.jpa.IGenericDaoJpa"&gt;
 *          &lt;constructor-arg value="org.appfuse.model.Foo"/&gt;
 *      &lt;/bean&gt;
 * </pre>
 *
 * @author <a href="mailto:bwnoll@gmail.com">Bryan Noll</a>
 *      Updated by jgarcia: update hibernate3 to hibernate4
 * @author jgarcia (update: added full text search + reindexing)
 * @param <T> a type variable
 * @param <PK> the primary key for that type
 */
public class GenericDaoJpa<T, PK extends Serializable> implements IGenericDao<T, PK> {
    /**
     * Log variable for all child classes. Uses LogFactory.getLog(getClass()) from Commons Logging
     */
    protected final Log log = LogFactory.getLog(getClass());
    private Class<T> persistentClass;
    private SessionFactory sessionFactory;
    private Querydsl querydsl;


    @PersistenceContext
    private javax.persistence.EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }


    @PostConstruct
    private void initQueryDsl(){
        SimpleEntityPathResolver resolver = SimpleEntityPathResolver.INSTANCE;
        EntityPath<T> path = resolver.createPath(persistentClass);
        PathBuilder<T> builder = new PathBuilder<T>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(entityManager, builder);
    }


    public GenericDaoJpa() {
        this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public GenericDaoJpa(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }


    public SessionFactory getSessionFactory() {
        Session session = (Session) entityManager.getDelegate();
        return session.getSessionFactory();
    }



/*    @Deprecated
    public Session getSession() throws HibernateException {
        Session sess = null;
        try{
            sess = getSessionFactory().getCurrentSession();
        }catch (org.hibernate.HibernateException e){

        }
        if (sess == null) {
            sess = getSessionFactory().openSession();
        }
        return sess;
    }*/


    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAll() {
        return this.entityManager.createQuery(
                "select obj from " + this.persistentClass.getName() + " obj")
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @SuppressWarnings("unchecked")
    public List<T> getAllDistinct() {
        Collection result = new LinkedHashSet(getAll());
        return new ArrayList(result);
    }

    /**
     * {@inheritDoc}
     */
    public T get(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);

        if (entity == null) {
            String msg = "Uh oh, '" + this.persistentClass + "' object with id '" + id + "' not found...";
            log.warn(msg);
            throw new EntityNotFoundException(msg);
        }

        return entity;
    }

    /**
     * {@inheritDoc}
     */
    public boolean exists(PK id) {
        T entity = this.entityManager.find(this.persistentClass, id);
        return entity != null;
    }

    /**
     * {@inheritDoc}
     */
    public T save(T object) {
        return this.entityManager.merge(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(T object) {
        this.entityManager.remove(object);
    }

    /**
     * {@inheritDoc}
     */
    public void remove(PK id) {
        this.entityManager.remove(this.get(id));
    }

    @Override
    public JPAQuery createQuery(EntityPath<?>... paths) {
        return (JPAQuery) querydsl.createQuery(paths);
    }


    @Override
    public JPAQuery createQuery() {
        return (JPAQuery) querydsl.createQuery();
    }

    @Override
    public JPQLQuery applyPagination(Pageable pageable, JPQLQuery query) {
        return querydsl.applyPagination(pageable, query);
    }

    @Override
    public JPQLQuery applySorting(Sort sort, JPQLQuery query) {
        return querydsl.applySorting(sort, query);
    }





}
