package io.github.stekeblad.mynotesapp.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;

@Entity
public class NoteItem {

    @Id
    @Index(unique = true)
    private Long id;

    @NotNull
    private String header;

    @NotNull
    private String description;


    @Generated(hash = 1613116906)
    public NoteItem(Long id, @NotNull String header, @NotNull String description) {
        this.id = id;
        this.header = header;
        this.description = description;
    }

    @Generated(hash = 260707407)
    public NoteItem() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
