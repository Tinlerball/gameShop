package web.game;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class Game {
    private String name;
    private String eddition;
    private String genre[];
    private String platform;
    private String cost;
}
