package com.arttelit.chibisov.simplestock.dao;

import com.arttelit.chibisov.simplestock.exceptions.ForbiddenException;
import com.arttelit.chibisov.simplestock.exceptions.NotFoundException;
import com.arttelit.chibisov.simplestock.models.Product;
import com.arttelit.chibisov.simplestock.models.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@Component
public class StockDAO {
    private JdbcTemplate jdbcTemplate;
    private static final ConcurrentMap<String,Boolean> STATUS_CONTROLLER = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String,List<Product>> CACHE = new ConcurrentHashMap<>();

    @Autowired
    public StockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(String store, String product, int count) throws NotFoundException {
        Integer id;

        try {
            id = jdbcTemplate.queryForObject("SELECT id FROM store WHERE name=?", Integer.class, store);
        } catch (Exception e) {
            throw new NotFoundException();
        }

        jdbcTemplate.update("INSERT INTO storage(store_id, product, count)\n" +
                "VALUES (?,?,?)\n" +
                "ON DUPLICATE KEY UPDATE count = storage.count + ?", id, product, count, count);

        STATUS_CONTROLLER.put(store,true);
    }

    public Storage getAll(String store){
        Storage storage = new Storage();
        List<Product> products;

        if(CACHE.containsKey(store) && !STATUS_CONTROLLER.get(store))
            products = CACHE.get(store);
        else {
            products =
                    jdbcTemplate.query("SELECT storage.product,storage.count FROM storage JOIN store ON storage.store_id=store.id WHERE store.name=?", new ProductMapper(), store);
            CACHE.put(store,products);
        }
        storage.setStoreName(store);
        storage.setProducts(products);

        return storage;
    }

    public void delete(String store, String product, int count) throws ForbiddenException {
        try {
            // Update product's count for selected store
            jdbcTemplate.update("UPDATE storage JOIN store ON storage.store_id=store.id SET storage.count=(storage.count - ?) WHERE store.name=? AND storage.product=?", count, store, product);
            // Delete product from store if count = 0
            jdbcTemplate.update("DELETE FROM storage WHERE storage.count = 0");
        } catch (Exception e) {
            throw new ForbiddenException();
        }
        STATUS_CONTROLLER.put(store,true);
    }
}
