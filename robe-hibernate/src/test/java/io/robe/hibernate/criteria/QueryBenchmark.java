package io.robe.hibernate.criteria;

import io.robe.hibernate.BenchMark;
import io.robe.hibernate.HibernateUtil;
import io.robe.hibernate.criteria.api.Criteria;
import io.robe.hibernate.criteria.hql.TransformerImpl;
import io.robe.hibernate.test.entity.Role;
import io.robe.hibernate.test.entity.User;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by kamilbukum on 24/01/2017.
 */
public class QueryBenchmark {
    protected static SessionFactory sessionFactory;
    //@BeforeClass
    public static void beforeClass(){
        if(sessionFactory == null) {
            sessionFactory = HibernateUtil.getSessionFactory(QueryBenchmark.class);
            Session session = sessionFactory.openSession();


            Set<String> nickNames = new LinkedHashSet<>();
            nickNames.add("Example");
            for(int i = 0 ; i < 1000; i++) {
                session.getTransaction().begin();
                Role role = new Role("TestRole_" + i, "Example First Role");
                session.persist(role);
                session.persist(new User(
                        "test" + i + "@robe.io",
                        "Kamil",
                        "Bukum",
                        "35345435345345",
                        true,
                        10,
                        new Date(),
                        new Date(),
                        role.getOid(),
                        "Transient Value",
                        nickNames
                ));
                session.getTransaction().commit();
            }
        }
    }

    // @Test
    public void benchmark(){
        System.out.println("HQL & ROBE CRITERIA Benchmarks started...");
        // HQL Example
        System.out.println("***** HIBERNATE HQL *******");
        Session session = sessionFactory.openSession();
        long startMilliSecond = System.currentTimeMillis();
        String hql = "SELECT user from io.robe.hibernate.test.entity.User user";
        List<User> userList = session.createQuery(hql).list();
        System.out.println(userList.size());
        long endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));


        // Criteria Example
        System.out.println("***** ROBE CRITERIA *******");
        session = sessionFactory.openSession();
        startMilliSecond = System.currentTimeMillis();
        userList = Criteria.createCriteria(User.class, new TransformerImpl<User>(session)).list();
        System.out.println(userList.size());
        endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));



        // HQL Example
        System.out.println("***** HIBERNATE HQL *******");
        session = sessionFactory.openSession();
        startMilliSecond = System.currentTimeMillis();
        hql = "SELECT user from io.robe.hibernate.test.entity.User user";
        userList = session.createQuery(hql).list();
        System.out.println(userList.size());
        endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));


        // Criteria Example
        System.out.println("***** ROBE CRITERIA *******");
        session = sessionFactory.openSession();
        startMilliSecond = System.currentTimeMillis();
        userList = Criteria.createCriteria(User.class, new TransformerImpl<User>(session)).list();
        System.out.println(userList.size());
        endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));


        // HQL Example
        System.out.println("***** HIBERNATE HQL *******");
        session = sessionFactory.openSession();
        startMilliSecond = System.currentTimeMillis();
        hql = "SELECT user from io.robe.hibernate.test.entity.User user";
        userList = session.createQuery(hql).list();
        System.out.println(userList.size());
        endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));


        // Criteria Example
        System.out.println("***** ROBE CRITERIA *******");
        session = sessionFactory.openSession();
        startMilliSecond = System.currentTimeMillis();
        userList = Criteria.createCriteria(User.class, new TransformerImpl<User>(session)).list();
        System.out.println(userList.size());
        endMilliSecond = System.currentTimeMillis();
        System.out.println("Milli Second ! -> : " + (endMilliSecond - startMilliSecond));

    }

    private static void benchMarkHqlJoin(int benchmarkCount){
        BenchMark.bench("HIBERNATE HQL", benchmarkCount, ()-> {
            Session session = sessionFactory.openSession();
            String hql = "SELECT user from io.robe.hibernate.test.entity.User user" +
                    "\nLEFT OUTER JOIN io.robe.hibernate.test.entity.Role role" +
                    "\nON user.roleOid=role.oid";
            List<User> userList = session.createQuery(hql).list();
            System.out.println(userList.size());
        });
    }

    private static void benchMarkRobeCriteriaJoin(int benchmarkCount){
        BenchMark.bench("ROBE CRITERIA", benchmarkCount, ()-> {
            Session session = sessionFactory.openSession();
            List<User> userList = Criteria.createCriteria(User.class, new TransformerImpl<User>(session))
                    .createJoin(Role.class, "roleOid").getRoot().list();
            System.out.println(userList.size());
        });
    }


    // @Test
    public void benchmark2(){
        String hql = "SELECT user from io.robe.hibernate.test.entity.User user";
        Session session = sessionFactory.openSession();
        List<User> userList = session.createQuery(hql).list();
        int benchmarkCount = 10;
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
        benchMarkHqlJoin(benchmarkCount);
        benchMarkRobeCriteriaJoin(benchmarkCount);
    }

}
