package com.example.Here.domain.invitation.repository;

import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    @EntityGraph(attributePaths = {"card", "receiver"})
    Page<Invitation> findByReceiver(Member receiver, Pageable pageable);

}
