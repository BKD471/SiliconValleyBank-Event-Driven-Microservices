package com.siliconvalley.accountsservices.dto.baseDtos;




public record RoleDto(String roleId,String roleName){

    public static final class Builder{
        private String roleId;
        private String roleName;

        public Builder(){}

        public Builder roleId(String roleId){
            this.roleId=roleId;
            return this;
        }

        public Builder roleName(String roleName){
            this.roleName=roleName;
            return this;
        }

        public RoleDto builder(){
            return new RoleDto(roleId,roleName);
        }
    }
}

