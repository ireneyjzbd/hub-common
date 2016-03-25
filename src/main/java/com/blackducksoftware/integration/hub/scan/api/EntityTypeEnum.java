package com.blackducksoftware.integration.hub.scan.api;


public enum EntityTypeEnum {

    RL // Release? Used for owner Entity Type
    ,
    CL // Code Location? Used for the asset Entity Type
    ,
    UNKNOWNENTITY;

    public static EntityTypeEnum getEntityTypeEnum(String entityType) {
        if (entityType == null) {
            return EntityTypeEnum.UNKNOWNENTITY;
        }
        EntityTypeEnum entityTypeEnum;
        try {
            entityTypeEnum = EntityTypeEnum.valueOf(entityType.toUpperCase());
        } catch (IllegalArgumentException e) {
            // ignore expection
            entityTypeEnum = UNKNOWNENTITY;
        }
        return entityTypeEnum;
    }

}