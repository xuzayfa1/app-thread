package uz.zero.post

import jakarta.persistence.*
import org.hibernate.annotations.ColumnDefault
import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.util.*


@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
open class BaseEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,

    @CreatedDate @Temporal(TemporalType.TIMESTAMP) var createdDate: Date? = null,

    @CreatedBy var createdBy: Long? = null,

    @Column(nullable = false) @ColumnDefault(value = "false") var deleted: Boolean = false,
)

@Entity
@Table(name = "posts")
class Post(
    var userId: Long,
    var content: String,

    @ElementCollection(fetch = FetchType.EAGER) 
    var mediaUrls: MutableList<String> = mutableListOf(),

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    var comments: MutableList<Comment> = mutableListOf(),

    @ElementCollection(fetch = FetchType.EAGER) 
    var likedUserIds: MutableSet<Long> = mutableSetOf(),

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", unique = true)
    var parent: Post? = null,

    var rootId: Long? = null
) : BaseEntity()

@Entity
@Table(name = "comments")
class Comment(
    var userId: Long,

    @Column(length = 1000)
    var text: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post
) : BaseEntity()