package com.newbini.newbeinquiz.Repository;

import com.newbini.newbeinquiz.domain.Bookmark;
import com.newbini.newbeinquiz.domain.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    List<Bookmark> findAllByMemberId(Long memberId);
}
