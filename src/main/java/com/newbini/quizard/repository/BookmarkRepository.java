package com.newbini.quizard.repository;

import com.newbini.quizard.domain.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByMemberId(Long memberId);
}
