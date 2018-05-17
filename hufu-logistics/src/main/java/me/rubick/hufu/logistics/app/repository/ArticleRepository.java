package me.rubick.hufu.logistics.app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import me.rubick.hufu.logistics.app.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Integer> {


}
