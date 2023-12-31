package com.example.Here.domain.invitation.repository;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.invitation.entity.Invitation;
import com.example.Here.domain.member.entity.Member;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Long> {

    Optional<Invitation> findByReceiverAndCard(Member receiver, Card card);

    Optional<Invitation> findByCardIdAndReceiver(String cardId, Member receiver);

    @EntityGraph(attributePaths = {"receiver"})
    List<Invitation> findByCardId(String cardId);

    @Modifying
    @Query("UPDATE Invitation i SET i.deleted = :deleted WHERE i.receiver = :member")
    void updateDeletedStatusForInvitationsReceivedByMember(Member member, Boolean deleted);

    @Modifying
    @Query("UPDATE Invitation i SET i.deleted = :deleted WHERE i.card = :card")
    void updateDeletedStatusForInvitationsByCard(Card card, Boolean deleted);

}
