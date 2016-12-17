package fr.k2i.adbeback.webapp.facade;

import fr.k2i.adbeback.core.business.media.Artist;
import fr.k2i.adbeback.core.business.media.Productor;
import fr.k2i.adbeback.core.business.user.User;
import fr.k2i.adbeback.dao.IArtistDao;
import fr.k2i.adbeback.webapp.bean.ArtistBean;
import fr.k2i.adbeback.webapp.bean.MusicBean;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableOrder;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableQuery;
import fr.k2i.adbeback.webapp.bean.datatable.query.DataTableSearch;
import fr.k2i.adbeback.webapp.bean.datatable.response.DataTableResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by dev on 27/01/15.
 */
@Service
public class ArtistFacade {

    @Autowired
    private UserFacade userFacade;

    @Autowired
    private IArtistDao artistDao;

    public DataTableResponse<MusicBean> findArtistByQuery(DataTableQuery query, List<String> relationObject) {


        DataTableResponse res = new DataTableResponse();
        res.setDraw(query.getDraw());

        DataTableSearch searchObj = query.getSearch();
        String value = searchObj.getValue();


        Integer length = query.getLength();
        Integer start = query.getStart();
        List<Sort.Order> orders = new ArrayList<>();


        List<DataTableOrder> orderObj = query.getOrder();
        orders.addAll(orderObj.stream().map(dataTableOrder -> new Sort.Order(dataTableOrder.toDirection(), relationObject.get(dataTableOrder.getColumn()))).collect(Collectors.toList()));
        Sort sort = new Sort(orders);

        Pageable pageable = new PageRequest(start/length,length,sort);

        Page<Artist> artists = null;

        User currentUser = userFacade.getCurrentUser();
        if (currentUser.getIdentity() instanceof Productor) {
            Productor productor = (Productor) currentUser.getIdentity();
            artists = artistDao.findArtistByLabel(value, sort,productor,pageable);
        }else{
            artists = artistDao.findArtist(value, sort, pageable);
        }


        List<ArtistBean> content = new ArrayList<>();

        for (Artist artist : artists) {
            content.add(new ArtistBean(artist));
        }


        res.setData(content);
        res.setRecordsFiltered(artists.getTotalElements());
        res.setRecordsTotal(artists.getTotalElements());

        return res;
    }
}
