package uz.zero.post

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.jpa.repository.support.JpaEntityInformation
import org.springframework.data.jpa.repository.support.SimpleJpaRepository
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.repository.query.Param

@NoRepositoryBean
interface BaseRepository<T : BaseEntity> : JpaRepository<T, Long>, JpaSpecificationExecutor<T> {
    fun findByIdAndDeletedFalse(id: Long): T?
    fun trash(id: Long): T?
    fun trashList(ids: List<Long>): List<T?>
    fun findAllNotDeleted(): List<T>
}


class BaseRepositoryImpl<T : BaseEntity>(
    entityInformation: JpaEntityInformation<T, Long>, entityManager: EntityManager
) : SimpleJpaRepository<T, Long>(entityInformation, entityManager), BaseRepository<T> {

    val isNotDeletedSpecification = Specification<T> { root, _, cb -> cb.equal(root.get<Boolean>("deleted"), false) }

    override fun findByIdAndDeletedFalse(id: Long) = findByIdOrNull(id)?.run { if (deleted) null else this }

    @Transactional
    override fun trash(id: Long): T? = findByIdOrNull(id)?.run {
        deleted  = true
        save(this)
    }

    override fun findAllNotDeleted(): List<T> = findAll(isNotDeletedSpecification)
    override fun trashList(ids: List<Long>): List<T?> = ids.map { trash(it) }
}

interface PostRepository : BaseRepository<Post> {
    fun existsByParentIdAndDeletedFalse(parentId: Long): Boolean

    @Query("SELECT p FROM Post p WHERE (p.rootId = :rootId OR p.id = :rootId) AND p.deleted = false ORDER BY p.createdDate ASC")
    fun findFullThread(@Param("rootId") rootId: Long): List<Post>

    fun findAllByRootIdAndDeletedFalseOrderByCreatedDateAsc(rootId: Long): List<Post>

    fun findAllByIdOrRootIdAndDeletedFalseOrderByCreatedDateAsc(id: Long, rootId: Long): List<Post>
}

interface CommentRepository : BaseRepository<Comment> {
    fun findAllByPostId(postId: Long): List<Comment>
}