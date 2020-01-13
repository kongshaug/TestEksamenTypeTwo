package entities;

import entities.User;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-11-15T11:16:38")
@StaticMetamodel(Role.class)
public class Role_ { 

    public static volatile ListAttribute<Role, User> userList;
    public static volatile SingularAttribute<Role, String> roleName;

}