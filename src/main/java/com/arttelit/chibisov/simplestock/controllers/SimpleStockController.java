package com.arttelit.chibisov.simplestock.controllers;

import com.arttelit.chibisov.simplestock.dao.StockDAO;
import com.arttelit.chibisov.simplestock.exceptions.ForbiddenException;
import com.arttelit.chibisov.simplestock.exceptions.NotFoundException;
import com.arttelit.chibisov.simplestock.models.Storage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping
public class SimpleStockController {
    private final StockDAO stockDAO;

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

        return new ResponseEntity(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
    }

    // http://localhost:8080//getAll?store=5chka
    @GetMapping("/getAll")
    public Storage getAll(@RequestParam("store") String store) {

        return stockDAO.getAll(store);
    }

    // http://localhost:8080//delete?store=5chka&product=apple&count=150
    @DeleteMapping ("/delete")
    public ResponseEntity delete(@RequestParam("store") String store,
                         @RequestParam("product") String product,
                         @RequestParam("count") int count) throws ForbiddenException {

        stockDAO.delete(store,product,count);

        return new ResponseEntity(HttpStatus.OK.getReasonPhrase(),HttpStatus.OK);
    }
}
