-- function for searching homes
create or replace function search_home(
    i_region varchar default null,
    i_district varchar default null,
    i_status varchar default null,
    i_home_type varchar default null,
    i_number_of_rooms integer default null,
    i_min_price double precision default null,
    i_max_price double precision default null
) returns setof home
    language plpgsql as
$$
declare
    v_condition varchar := '';
begin

    if i_region <> '' then
        v_condition := v_condition || ' and region=' || '''' || i_region || '''';
    end if;
    if i_district <> '' then
        v_condition := v_condition || ' and district=' || '''' || i_district || '''';
    end if;
    if i_status <> '' then
        v_condition := v_condition || ' and status=' || '''' || i_status || '''';
    end if;
    if i_home_type <> '' then
        v_condition := v_condition || ' and home_type=' || '''' || i_home_type || '''';
    end if;
    if i_number_of_rooms <> -1 then
        v_condition := v_condition || ' and number_of_rooms=' || i_number_of_rooms;
    end if;
    if i_min_price <> -1 then
        v_condition := v_condition || ' and price >= ' || i_min_price;
    end if;
    if i_max_price <> -1 then
        v_condition := v_condition || ' and price <= ' || i_max_price;
    end if;
    return query execute 'select * from home where active and not ban' || v_condition || ' order by created_date';
end;
$$;


-- select * from search_home(i_region := null,
--                    i_district := null,
--                    i_status := null,
--                    i_home_type := null,
--                    i_number_of_rooms := null,
--                    i_min_price := null,
--                    i_max_price := null);

-- function for changing number of likes by homeId and userId
create or replace function change_home_like(
    i_home_id uuid,
    i_user_id uuid
) returns setof likes
    language plpgsql as
$$
declare
    v_active boolean;
begin

    perform id from users where id = i_user_id;
    if not FOUND then return next null; end if;

    perform id from home where id = i_home_id;
    if not FOUND then return next null; end if;

    perform id from likes where user_id = i_user_id and home_id = i_home_id;
    if FOUND then
        update likes
        set active=not active
        where home_id = i_home_id
          and user_id = i_user_id
        returning active into v_active;

        if v_active then
            update home set likes=likes + 1 where id = i_home_id;
        else
            update home set likes=likes - 1 where id = i_home_id;
        end if;
    else
        insert into likes(home_id, user_id, active)
        VALUES (i_home_id, i_user_id, true);
        update home set likes=likes + 1 where id = i_home_id;
    end if;

    return query select *
                 from likes
                 where home_id = i_home_id
                   and user_id = i_user_id;

exception
    when others then
        return next null;
end;
$$;

-- select * from change_home_like('1c1e9af6-e544-4b52-b81f-de2f12922cd4', '42024569-6692-4629-8557-e55639351e4f');

-- function for changing number of likes by likeId
create or replace function change_home_like(
    i_like_id uuid
) returns setof likes
    language plpgsql as
$$
declare
    v_data likes;
begin

    perform id from likes where id = i_like_id;
    if not FOUND then return next null; end if;

    update likes
    set active=not active
    where id = i_like_id
    returning * into v_data;

    if v_data.active then
        update home set likes=likes + 1 where id = v_data.home_id;
    else
        update home set likes=likes - 1 where id = v_data.home_id;
    end if;

    return query select * from likes where id = i_like_id;

exception
    when others then
        return next null;
end;
$$;

-- select * from change_home_like('8d1e8ab5-adb0-4445-a631-87ddb5ed9445');


create or replace procedure delete_all_homes()
    language plpgsql as
$$
begin
    delete from home_attachments;
    delete from attachment;
    delete from home ;
    update users set crt_page=0 where crt_page > 0;
end;
$$;

-- call delete_all_homes();



