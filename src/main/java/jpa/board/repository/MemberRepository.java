package jpa.board.repository;

import jpa.board.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    public List<Member> findByName(String name);

    Optional<Member> findByEmail(String email);
}
