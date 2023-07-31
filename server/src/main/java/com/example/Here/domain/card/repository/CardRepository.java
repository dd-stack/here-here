package com.example.Here.domain.card.repository;

import com.example.Here.domain.card.entity.Card;
import com.example.Here.domain.member.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends JpaRepository<Card, String> {

    @EntityGraph(attributePaths = {"creator"})
    Page<Card> findByCreator(Member creator, Pageable pageable);

    @Query("select i.card from Invitation i where i.receiver = :member")
    Page<Card> findCardsByReceiver(Member member, Pageable pageable);
}
