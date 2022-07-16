package jpql;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        // persistence.xml에 있는 <persistence-unit name="hello"> 얘를 불러온다.
        // EntityManagerFactory 애플리케이션 로딩 시점에 딱 하나만 만들어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        // 실제 DB에 저장하거나 하는 트랜잭션 단위를 할 때마다 EntityManager를 만든다.
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        // 엔티티 매니저를 꺼낸 후 여기서 실제 코드를 작성
        try {
            Member member = new Member();
            member.setUsername("member1");
            member.setAge(10);
            em.persist(member);

            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
            TypedQuery<String> query2 = em.createQuery("select m.username from Member m", String.class);
            // 아래처럼 타입을 특정하여 명기할 수 없을 때는 타입쿼리 말고 그냥 쿼리를 쓴다.
            Query query3 = em.createQuery("select m.username, m.age from Member m");

            List<Member> resultList = query1.getResultList();
            Member singleResult = query1.getSingleResult();

            // 체이닝 룰 이용해서 써도 된다.
            String jpql = "select m from Member m where m.username = :username";
            TypedQuery<Member> query4 = em.createQuery(jpql, Member.class);
            query4.setParameter("username", "member1");
            Member singleResult1 = query4.getSingleResult();
            System.out.println("singleResult1 = " + singleResult1);

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            em.close();
        }
        emf.close();
    }
}
