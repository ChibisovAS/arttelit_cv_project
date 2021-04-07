package com.arttelit.chibisov.simplestock.dao;

import com.arttelit.chibisov.simplestock.models.Product;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ProductMapper implements RowMapper<Product> {

    @Override
    public  Product mapRow(ResultSet resultSet, int i) throws SQLException {
        Product product = new Product();

        product.setName(resultSet.getString("product"));
        product.setCount(resultSet.getInt("count"));

        return product;
    }
}
