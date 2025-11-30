package com.taskmanager.usermanagement.repository.specification;

import java.util.UUID;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.taskmanager.common.enums.Status;
import com.taskmanager.common.util.StringUtils;

public class UserSpecification {

    /**
     * Build a MongoDB Query that matches a user by identifier (UUID, ObjectId, email or username).
     * Returns an empty Query if identifier is null/blank.
     */
    public static Query matchIdentifier(String identifier) {
        Query query = new Query();
        if (identifier == null || identifier.trim().isEmpty()) {
            return query;
        }

        if (StringUtils.isValidUUID.test(identifier)) {
            query.addCriteria(Criteria.where("id").is(UUID.fromString(identifier)));
        } else if (ObjectId.isValid(identifier)) {
            query.addCriteria(Criteria.where("id").is(new ObjectId(identifier)));
        } else if (StringUtils.isValidEmail.test(identifier)) {
            query.addCriteria(Criteria.where("email").is(identifier));
        } else {
            query.addCriteria(Criteria.where("username").is(identifier));
        }
        return query;
    }

    /**
     * Build a MongoDB Query that matches a user by identifier and status.
     */
    public static Query matchIdentifier(String identifier, Status status) {
        Query query = matchIdentifier(identifier);
        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }
        return query;
    }
}
