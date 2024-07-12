
BEGIN
    RETURN QUERY
    WITH similar_users AS (
        SELECT cs.similar_user_id
        FROM calculate_top20_similar_users_cosine(input_user_id) cs
    ),
    neighbor_ratings AS (
        SELECT r.movie_id, AVG(r.rating) AS average_rating
        FROM ratings r
        WHERE r.user_id IN (SELECT su.similar_user_id FROM similar_users su)
        AND r.movie_id NOT IN (SELECT ir.movie_id FROM ratings ir WHERE ir.user_id = input_user_id)
        GROUP BY r.movie_id
    )
    SELECT nr.movie_id, nr.average_rating
    FROM neighbor_ratings nr
    ORDER BY nr.average_rating DESC
    LIMIT 100;  -- Adjust the limit as needed to get the top N recommendations
END;
