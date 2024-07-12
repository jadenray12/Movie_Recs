BEGIN
    RETURN QUERY
    WITH user_ratings AS (
        SELECT user_id, movie_id, rating
        FROM ratings
    ),
    input_user_ratings AS (
        SELECT movie_id, rating AS input_rating
        FROM ratings
        WHERE user_id = input_user_id
    ),
    similar_user_ratings AS (
        SELECT r.user_id, r.movie_id, r.rating AS similar_rating
        FROM user_ratings r
        JOIN input_user_ratings iur ON r.movie_id = iur.movie_id
        WHERE r.user_id != input_user_id
    ),
    dot_products AS (
        SELECT sur.user_id AS dp_user_id,
               SUM(iur.input_rating * sur.similar_rating) AS dot_product,
               SQRT(SUM(POWER(iur.input_rating, 2))) AS magnitude_input_user,
               SQRT(SUM(POWER(sur.similar_rating, 2))) AS magnitude_similar_user
        FROM input_user_ratings iur
        JOIN similar_user_ratings sur ON iur.movie_id = sur.movie_id
        GROUP BY sur.user_id
    ),
    cosine_similarities AS (
        SELECT dp.dp_user_id AS similar_user_id,
               (dp.dot_product / (dp.magnitude_input_user * dp.magnitude_similar_user)) AS cosine_similarity
        FROM dot_products dp
    )
    SELECT cs.similar_user_id, cs.cosine_similarity
    FROM cosine_similarities cs
    ORDER BY cs.cosine_similarity DESC
    LIMIT 20;
END;
