package com.halasa.criterialambda.dao.builder;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Path;

public class OrderBy {

    public enum OrderDirection { ASC, DESC }

    private final OrderDirection direction;
    private final Path<?> attributePath;

    private OrderBy(Path<?> attributePath, OrderDirection direction) {
        this.direction = direction;
        this.attributePath = attributePath;
    }

    public static OrderBy of(Path<?> attributePath, boolean ascending) {
        return new OrderBy(attributePath, ascending ? OrderDirection.ASC : OrderDirection.DESC);
    }

    public static OrderBy asc(Path<?> attributePath) {
        return new OrderBy(attributePath, OrderDirection.ASC);
    }

    public static OrderBy desc(Path<?> attributePath) {
        return new OrderBy(attributePath, OrderDirection.DESC);
    }

    public OrderDirection getDirection() {
        return direction;
    }

    public Path<?> getAttributePath() {
        return attributePath;
    }

    public Order toJpa(CriteriaBuilder cb) {
        return this.getDirection() == OrderDirection.ASC
                ? cb.asc(this.attributePath)
                : cb.desc(this.attributePath);
    }
}
