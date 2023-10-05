package top.chilfish.chilpost.service

import org.jetbrains.exposed.sql.insertIgnoreAndGetId
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import top.chilfish.chilpost.model.PostWithOwner
import top.chilfish.chilpost.model.toPosts
import top.chilfish.chilpost.utils.query

@Service
@Transactional
class PostService {
    fun getAll() = PostWithOwner.query("select * from post_with_owner").toPosts()

    fun getById(id: Int) = PostWithOwner.query("select * from post_details where id = $id").toPosts()

    fun newPost(content: String, ownerId: String) {
        val insertId = PostWithOwner.insertIgnoreAndGetId {
            it[PostWithOwner.content] = content
            it[PostWithOwner.ownerId] = ownerId.toInt()
        }!!

        return
    }

    fun newComment(content: String, ownerId: String, parentId: String) =
        PostWithOwner.query(
            "Insert Into posts (content, owner_id, parent_id, is_body)\n" +
                    "Select $content, $ownerId, $parentId, False\n" +
                    "Where Exists (Select 1 From posts Where id = $parentId)\n" +
                    "And Exists(Select 1 From users Where id = $ownerId);"
        )

}