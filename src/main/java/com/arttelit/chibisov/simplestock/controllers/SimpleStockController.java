package com.arttelit.chibisov.simplestock.controllers;

import com.arttelit.chibisov.simplestock.SimplestockApplication;
import com.arttelit.chibisov.simplestock.dao.StockDAO;
import com.arttelit.chibisov.simplestock.exceptions.ForbiddenException;
import com.arttelit.chibisov.simplestock.exceptions.NotFoundException;
import com.arttelit.chibisov.simplestock.models.Product;
import com.arttelit.chibisov.simplestock.models.Storage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;


@RestController
@RequestMapping
public class SimpleStockController {
    private final StockDAO stockDAO;

    static final Logger logger = LoggerFactory.getLogger(SimplestockApplication.class);
    private static final ConcurrentMap<String,Boolean> STATUS_CONTROLLER = new ConcurrentHashMap<>();
    private static final ConcurrentMap<String, List<Product>> CACHE = new ConcurrentHashMap<>();

    @Autowired
    public SimpleStockController(StockDAO stockDAO) {
        this.stockDAO = stockDAO;
    }

    // http://localhost:8080//add?store=5chka&product=apple&count=300
    @GetMapping("/add")
    public ResponseEntity add(@RequestParam("store") String store,
                              @RequestParam("product") String product,
                              @RequestParam("count") int count) throws NotFoundException {


        stockDAO.add(store,product,count);
        logger.debug(String.format(" /add query for %s successfully executed",store));
        STATUS_CONTROLLER.put(store,true);
        logger.debug("STATUS_CONTROLLER set to TRUE");

        return new ResponseEntity(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
    }

    // http://localhost:8080//getAll?store=5chka
    @GetMapping("/getAll")
    public Storage getAll(@RequestParam("store") String store) {

        Storage storage = new Storage();
        List<Product> products;

        if(CACHE.containsKey(store) && !STATUS_CONTROLLER.get(store))
            products = CACHE.get(store);
        else {
            products = stockDAO.getAll(store);
            CACHE.put(store,products);
            logger.debug(String.format("%s added to CACHE",store));

            STATUS_CONTROLLER.put(store,false);
            logger.debug("STATUS_CONTROLLER set to FALSE");
        }
        storage.setStoreName(store);
        storage.setProducts(products);
        logger.debug(String.format(" /getAll query for %s successfully executed",store));
        return storage;
    }

    // http://localhost:8080//delete?store=5chka&product=apple&count=150
    @DeleteMapping ("/delete")
    public ResponseEntity delete(@RequestParam("store") String store,
                         @RequestParam("product") String product,
                         @RequestParam("count") int count) throws ForbiddenException {

        stockDAO.delete(store,product,count);
        logger.debug(String.format(" /delete query for %s successfully executed",store));

        STATUS_CONTROLLER.put(store,true);
        logger.debug("STATUS_CONTROLLER set to TRUE");

        return new ResponseEntity(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
    }
}
