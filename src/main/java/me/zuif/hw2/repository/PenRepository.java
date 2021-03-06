package me.zuif.hw2.repository;


import me.zuif.hw2.model.pen.Pen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class PenRepository implements ProductRepository<Pen> {
    private final List<Pen> pens;
    private final Logger logger = LoggerFactory.getLogger(PenRepository.class);

    public PenRepository() {
        pens = new LinkedList<>();
    }

    @Override
    public void save(Pen pen) {
        if (pen == null) {
            final IllegalArgumentException exception = new IllegalArgumentException("Cannot save a null pen");
            logger.error(exception.getMessage(), exception);
            throw exception;
        } else {
            checkDuplicates(pen);
            pens.add(pen);
        }
    }

    private void checkDuplicates(Pen pen) {
        for (Pen p : pens) {
            if (pen.hashCode() == p.hashCode() && pen.equals(p)) {
                final IllegalArgumentException exception = new IllegalArgumentException("Duplicate pen: " +
                        pen.getId());
                logger.error(exception.getMessage(), exception);
                throw exception;
            }
        }
    }

    @Override
    public void saveAll(List<Pen> pens) {
        for (Pen pen : pens) {
            save(pen);
        }
    }

    @Override
    public boolean update(Pen pen) {
        final Optional<Pen> result = findById(pen.getId());
        if (result.isEmpty()) {
            return false;
        }
        final Pen originPen = result.get();
        PenCopy.copy(pen, originPen);
        return true;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<Pen> iterator = pens.iterator();
        while (iterator.hasNext()) {
            final Pen pen = iterator.next();
            if (pen.getId().equals(id)) {
                logger.info("Deleting {}", pen);
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Pen> findAll() {
        if (pens.isEmpty()) {
            return Collections.emptyList();
        }
        return pens;
    }

    @Override
    public Optional<Pen> findById(String id) {
        Pen result = null;
        for (Pen pen : pens) {
            if (pen.getId().equals(id)) {
                result = pen;
            }
        }
        return Optional.ofNullable(result);
    }

    private static class PenCopy {
        private static void copy(final Pen from, final Pen to) {
            to.setCount(from.getCount());
            to.setPrice(from.getPrice());
            to.setTitle(from.getTitle());
        }
    }
}
