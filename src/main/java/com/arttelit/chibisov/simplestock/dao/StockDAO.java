package com.arttelit.chibisov.simplestock.dao;

import com.arttelit.chibisov.simplestock.SimplestockApplication;
import com.arttelit.chibisov.simplestock.exceptions.ForbiddenException;
import com.arttelit.chibisov.simplestock.exceptions.NotFoundException;
import com.arttelit.chibisov.simplestock.models.Product;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class StockDAO {
    private JdbcTemplate jdbcTemplate;

    static final Logger logger = LoggerFactory.getLogger(SimplestockApplication.class);

    @Autowired
    public StockDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(String store, String product, int count) throws NotFoundException {
        Integer id;
        try {
            id = jdbcTemplate.queryForObject("SELECT id FROM store WHERE name=?", Integer.class, store);
        } catch (Exception e) {
            logger.debug("Store not found");
            throw new NotFoundException();
        }
        jdbcTemplate.update("INSERT INTO storage(store_id, product, count)\n" +
                "VALUES (?,?,?)\n" +
                "ON DUPLICATE KEY UPDATE count = storage.count + ?", id, product, count, count);

    }

    public List<Product> getAll(String store){
        List<Product> products =
                    jdbcTemplate.query("SELECT storage.product,storage.count FROM storage JOIN store ON storage.store_id=store.id WHERE store.name=?", new ProductMapper(), store);
        return products;
    }

    public void delete(String store, String product, int count) throws ForbiddenException {
        try {
            // Update product's count for selected store
            jdbcTemplate.update("UPDATE storage JOIN store ON storage.store_id=store.id SET storage.count=(storage.count - ?) WHERE store.name=? AND storage.product=?", count, store, product);
            // Delete product from store if count = 0
            jdbcTemplate.update("DELETE FROM storage WHERE storage.count = 0");
        } catch (Exception e) {
            logger.debug("Forbidden operation");
            throw new ForbiddenException();
        }
    }
}
