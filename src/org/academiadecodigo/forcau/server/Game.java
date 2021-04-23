package org.academiadecodigo.forcau.server;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import org.academiadecodigo.forcau.Color;

public class Game {

    public static Vector<UserHandler> players;
    private HashSet<String> usedCharacters;
    private List<String> map;
    private String[] wordChars;
    private String word;
    private String tips;
    private String underscores;
    private String charactersNotGuessed = "";
    private String charGuessed;
    private final int maxTries = 9;
    private int tries = 0;
    private int charactersGuessed = 0;
    private int counter = 0;
    public boolean start;
    private boolean multiplayer;
    private UserHandler p1;
    private static boolean chooser;
    private boolean right = false;

    /**
     * Creates game and adds first player
     * Gametype 1: one player
     * Gametype 2: multiplayer
     *
     * @param
     * @param
     */
    public Game() {

        map = new ArrayList<>();
        usedCharacters = new HashSet<>();
        start = false;

    }

    //Setters


    public static void setChooser(boolean chooser) {
        Game.chooser = chooser;
    }

    //Custom Methods
    public void start(UserHandler firstPlayer, boolean multiplayer) {

        players = new Vector<>();
        players.add(firstPlayer);
        this.multiplayer = multiplayer;
        p1 = players.get(0);

        if (!multiplayer) {
            soloGame();
        } else {
            multiplayerGame();
        }

    }

    private void soloGame() {
        readfile();
        p1.systemMessage(Color.PURPLE_BOLD + "The word has " + randomWord() + " characters." + Color.RESET);
        p1.systemMessage(Color.PURPLE_BOLD + underscores + Color.RESET);

        gameLogic();
        resetProperties();
        restart();
    }

    private void multiplayerGame() {

        int countDown = 20;

        while (countDown >= 0) {
            try {
                if (countDown < 10) {
                    p1.dispatchMessage(Color.RED + "Players have: " + countDown + " seconds to join" + Color.RESET);
                    Thread.sleep(1000);
                    countDown--;
                } else {
                    p1.dispatchMessage(Color.GREEN + "Players have: " + countDown + " seconds to join" + Color.RESET);
                    Thread.sleep(5000);
                    countDown = countDown - 5;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        if (players.size() == 1) {
            p1.dispatchMessage(Color.RED_BOLD + "\nNobody entered your game bro\nYou will return to initial menu." + Color.RESET);
            p1.broadCast(Color.RED_BOLD + "Game didn't had enough players to start." + Color.RESET);

            restart();
            return;
        } else {
            p1.systemMessage(Color.GREEN_BOLD + "Game started BRO" + Color.RESET + "\n");
        }

        readfile();
        start = true;
        p1.systemMessage(Color.PURPLE_BOLD + "The word has " + randomWord() + " characters." + Color.RESET);
        p1.systemMessage(Color.PURPLE_BOLD + underscores + Color.RESET);

        gameLogic();
        resetProperties();
        restart();
    }

    public void restart() {

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        for (int i = players.size(); i > 0; i--) {

            players.get(i - 1).dispatchMessage("\n");
            players.get(i - 1).startMenu();

        }
    }

    public void checkChar(String charGuessed) {

        if (word.contains(charGuessed)) {
            right = true;
            boolean[] hiddenLetters = new boolean[word.length()];

            for (int i = 0; i < wordChars.length; i++) {
                if (wordChars[i].equals(charGuessed)) {
                    hiddenLetters[i] = true;
                    charactersGuessed++;
                }
            }

            displayLetter(hiddenLetters, charGuessed);
            players.get(0).systemMessage(Color.GREEN + "Character " + charGuessed + " found!\n" + underscores + Color.RESET);

        } else {
            players.get(0).systemMessage(Color.RED + charGuessed + " is not on the word bro :(" + Color.RESET);
            charactersNotGuessed += charGuessed + ", ";
            tries++;
        }

    }

    public boolean join(UserHandler newPlayer) {

        if (start) {
            return false;
        }

        players.add(newPlayer);
        return true;

    }

    /**
     * read word file
     */
    private void readfile() {
        String dados;
        String[] dados2;

        if (!chooser) {
            dados = "ze vivo no polo norte e tenho de comprar um mic novo\n" +
                    "regina Antonio Oliveira Salazar\n" +
                    "sara tenho resting bitch-face\n" +
                    "miguel gosto de abrir portais\n" +
                    "francisco if... then else??\n" +
                    "fabio o demonio\n" +
                    "lucianno fiz o workshop no WC\n" +
                    "caseiro anonymous\n" +
                    "hugo nao tenho carinho pelo codigo\n" +
                    "helio pontual\n" +
                    "almeida foi o meu sonho ser sub-delegado\n" +
                    "vania perdi para o sub-delegado\n" +
                    "margarida monica, marlene, marta\n" +
                    "leandro clap master\n" +
                    "isaac fim-de-semana para descansar\n" +
                    "vasco ovni miau\n" +
                    "jb multa + homework + summarizer + trabalho = dream day\n" +
                    "barroso hamburgueres\n" +
                    "dogaz ao menos tenho sabonetes\n" +
                    "gyosas colheres de conheshitmento\n" +
                    "patricia eu tenho uma filha de 4 anos\n" +
                    "luis usado em folha\n" +
                    "carlos torradeira\n" +
                    "emanuel array de arrays com arrays de arrays\n" +
                    "andre chego tarde\n" +
                    "ana gosto da megadrive\n" +
                    "bombom feito de cacau saboroso\n" +
                    "rute eu prefiro online\n" +
                    "jn almocei no cemiterio\n" +
                    "gaio melhor cara de desespero\n" +
                    "mcsara eu digo piadas lamentaveis\n" +
                    "mariana dancing-jesus-feelings-person\n" +
                    "paulo ojolhos e ajorelhas\n" +
                    "ricardo inho\n" +
                    "vando master...ando\n";
            dados2 = dados.split("\n");
        } else {
            dados = "abysmal extremely bad.\n" +
                    "adverse preventing success or development.\n" +
                    "alarming worrying.\n" +
                    "angry feeling or showing strong annoyance.\n" +
                    "annoy irritate.\n" +
                    "anxious unease about something with an uncertain outcome.\n" +
                    "apathy lack of interest, enthusiasm, or concern.\n" +
                    "appalling horrific.\n" +
                    "atrocious horrifyingly wicked.\n" +
                    "awful very bad or unpleasant.\n" +
                    "bad not such as to be hoped for or desired.\n" +
                    "banal so lacking in originality as to be obvious and boring.\n" +
                    "barbed deliberately hurtful.\n" +
                    "beneath at a lower level or layer than.\n" +
                    "boring not interesting.\n" +
                    "broken despairing.\n" +
                    "callous showing or having an insensitive and cruel disregard for others.\n" +
                    "clumsy awkward in movement or in handling things.\n" +
                    "corpse a dead body, especially of a human being rather than an animal.\n" +
                    "cold lacking affection or warmth of feeling; \n" +
                    "collapse suddenly fall down or give way.\n" +
                    "confused bewildered.\n" +
                    "contradictory mutually opposed or inconsistent.\n" +
                    "corrupt cause to act dishonestly in return for money or personal gain.\n" +
                    "crazy a mad person.\n" +
                    "creepy causing an unpleasant feeling of fear or unease.\n" +
                    "criminal a person who has committed a crime.\n" +
                    "cruel wilfully causing pain or suffering to others, or feeling no concern about it.\n" +
                    "damage a sum of money claimed or awarded in compensation for a loss or an injury.\n" +
                    "dead no longer alive.\n" +
                    "decaying rotting or decomposing through the action of bacteria and fungi.\n" +
                    "deformed not having the normal or natural shape or form.\n" +
                    "deny state that one refuses to admit the truth or existence of.\n" +
                    "deplorable deserving strong condemnation.\n" +
                    "depressed in a state of unhappiness or despondency.\n" +
                    "deprived suffering a severe and damaging lack of basic material and cultural benefits.\n" +
                    "despicable deserving hatred and contempt.\n" +
                    "dirty covered or marked with an unclean substance.\n" +
                    "disease a disorder of structure or function in a human, animal, or plant.\n" +
                    "disgusting arousing revulsion or strong indignation.\n" +
                    "dishonest intended to mislead or cheat.\n" +
                    "enraged furious.\n" +
                    "evil profoundly immoral and wicked.\n" +
                    "fail be unsuccessful in achieving one's goal.\n" +
                    "faulty not working or made correctly; having defects.\n" +
                    "fear an unpleasant emotion caused by the threat of danger, pain, or harm.\n" +
                    "feeble lacking physical strength, especially as a result of age or illness.\n" +
                    "fight take part in a violent struggle.\n" +
                    "filthy disgustingly dirty.\n" +
                    "foul a disgusting smell or taste.\n" +
                    "frighten afraid or anxious.\n" +
                    "hard solid, firm, and rigid.\n" +
                    "hate feel intense dislike for.\n" +
                    "hideous extremely ugly.\n" +
                    "horrendous extremely unpleasant, horrifying, or terrible.\n" +
                    "horrible causing or likely to cause horror.\n" +
                    "hostile showing or feeling opposition or dislike.\n" +
                    "hurt cause pain or injury to.\n" +
                    "icky nasty or unpleasant.\n" +
                    "ignorant lacking knowledge or awareness in general.\n" +
                    "imperfect faulty or incomplete.\n" +
                    "impossible not able to occur, exist, or be done.\n" +
                    "infernal relating to or characteristic of hell or the underworld.\n" +
                    "injure do physical harm or damage to (someone).\n" +
                    "insane seriously mentally ill.\n" +
                    "insidious proceeding in a gradual, subtle way, but with very harmful effects.\n" +
                    "jealous fiercely protective of one's rights or possessions.\n" +
                    "junky useless or of little value.\n" +
                    "lose be deprived of or cease to have or retain (something).\n" +
                    "lousy very poor or bad.\n" +
                    "lumpy full of or covered with lumps.\n" +
                    "malicious characterized by malice; intending or intended to do harm.\n" +
                    "menacing suggesting the presence of danger.\n" +
                    "messy untidy or dirty.\n" +
                    "misshapen not having the normal or natural shape or form.\n" +
                    "missing not present or included when expected or supposed to be.\n" +
                    "misunderstood incorrectly interpreted or understood.\n" +
                    "moan complain or grumble, typically about something trivial.\n" +
                    "moldy covered with a fungal growth which causes decay, due to age or damp conditions.\n" +
                    "monstrous inhumanly or outrageously evil or wrong.\n" +
                    "naive showing a lack of experience, wisdom, or judgement.\n" +
                    "nasty very bad or unpleasant.\n" +
                    "naughty badly behaved.\n" +
                    "negate make ineffective.\n" +
                    "negative a word or statement that expresses denial, disagreement, or refusal.\n" +
                    "never at no time in the past or future.\n" +
                    "nobody no one.\n" +
                    "nonsense spoken or written words that have no meaning or make no sense.\n" +
                    "noxious harmful, poisonous, or very unpleasant.\n" +
                    "objection an expression or feeling of disapproval or opposition.\n" +
                    "odious extremely unpleasant\n" +
                    "offensive causing someone to feel resentful, upset, or annoyed.\n" +
                    "old having lived for a long time.\n" +
                    "oppressive inflicting harsh and authoritarian treatment.\n" +
                    "pain highly unpleasant physical sensation caused by illness or injury.\n" +
                    "perturb anxious or unsettled.\n" +
                    "pessimistic tending to see the worst aspect of things or believe that the worst will happen.\n" +
                    "petty of little importance\n" +
                    "plain simple or basic in character.\n" +
                    "poisonous causing or capable of causing death or illness if taken into the body.\n" +
                    "poor lacking money to live at a standard considered comfortable or normal in a society.\n" +
                    "prejudice preconceived opinion that is not based on reason or actual experience.\n" +
                    "questionable doubtful as regards truth or validity.\n" +
                    "quirky having or characterized by peculiar or unexpected traits or aspects.\n" +
                    "quit leave (a place), usually permanently.\n" +
                    "reject dismiss as inadequate, unacceptable, or faulty.\n" +
                    "repellent impervious to a particular substance.\n" +
                    "repugnant extremely distasteful.\n" +
                    "repulsive arousing intense distaste or disgust.\n" +
                    "revenge inflict hurt or harm on someone for an injury or wrong done to oneself.\n" +
                    "revolting causing intense disgust.\n" +
                    "rotten suffering from decay.\n" +
                    "rude offensively impolite or bad-mannered.\n" +
                    "less a smaller amount of.\n" +
                    "sad feeling or showing sorrow.\n" +
                    "savage fierce, violent, and uncontrolled.\n" +
                    "scare cause great fear or nervousness in.\n" +
                    "scream give a long, loud, piercing cry or cries expressing extreme emotion or pain.\n" +
                    "severe very great.\n" +
                    "shocking causing indignation or disgust.\n" +
                    "shoddy badly made or done.\n" +
                    "sick affected by physical or mental illness.\n" +
                    "sickening causing or liable to cause a feeling of nausea or disgust.\n" +
                    "sinister giving the impression that something harmful or evil is happening or will happen.\n" +
                    "slimy covered by or resembling slime.\n" +
                    "smelly having a strong or unpleasant smell.\n" +
                    "sobbing noisy crying.\n" +
                    "sorry feeling sad or distressed through sympathy with someone else's misfortune.\n" +
                    "spiteful showing or caused by malice.\n" +
                    "sticky tending or designed to stick to things on contact.\n" +
                    "stinky having a strong or unpleasant smell.\n" +
                    "stormy characterized by strong winds and usually rain, thunder, lightning, or snow.\n" +
                    "stressful causing mental or emotional stress.\n" +
                    "stuck insert, thrust, or push.\n" +
                    "stupid having or showing a great lack of intelligence or common sense.\n" +
                    "substandard below the usual or required standard.\n" +
                    "suspect doubt the genuineness or truth of.\n" +
                    "suspicious having or showing a cautious distrust of someone or something.\n" +
                    "tense stretched tight or rigid.\n" +
                    "terrible extremely bad or serious.\n" +
                    "threatening threatening\n" +
                    "ugly unpleasant or repulsive, especially in appearance.\n" +
                    "undermine lessen the effectiveness, especially gradually or insidiously.\n" +
                    "unfair not based on or behaving according to the principles of equality and justice.\n" +
                    "unfavorable expressing or showing a lack of approval or support.\n" +
                    "unhappy unfortunate.\n" +
                    "unhealthy harmful to health.\n" +
                    "unjust not based on or behaving according to what is morally right and fair.\n" +
                    "unlucky having, bringing, or resulting from bad luck.\n" +
                    "unpleasant causing discomfort, unhappiness, or revulsion; disagreeable.\n" +
                    "unsatisfactory not satisfactory; not good enough.\n" +
                    "unsightly unpleasant to look at.\n" +
                    "unwanted not or no longer desired.\n" +
                    "unwholesome not characterized by or conducive to health or moral well-being.\n" +
                    "unwieldy difficult to move because of its size, shape, or weight.\n" +
                    "unwise not wise or sensible.\n" +
                    "upset make (someone) unhappy, disappointed, or worried.\n" +
                    "vice immoral or wicked behaviour.\n" +
                    "vicious deliberately cruel or violent.\n" +
                    "vile extremely unpleasant.\n" +
                    "villainous relating to, constituting, or guilty of wicked or criminal behaviour.\n" +
                    "vindictive having or showing a strong or unreasoning desire for revenge.\n" +
                    "wary feeling or showing caution about possible dangers or problems.\n" +
                    "weary feeling or showing extreme tiredness, especially as a result of excessive exertion.\n" +
                    "worthless having no real value or use.\n" +
                    "yell a loud, sharp cry of pain, surprise, or delight.\n" +
                    "yucky messy or disgusting.\n" +
                    "zero no quantity or number; nought; the figure 0.";
            dados2 = dados.split("\n");
        }


        for (int i = 0; i < dados2.length; i++) {

            String dica = "";
            String[] split = dados2[i].split(" ");
            map.add(split[0]);

            for (int ii = 1; ii < split.length; ii++) {

                if (ii == (split.length - 1)) {
                    dica += split[ii] + ".";
                    break;
                }

                dica += split[ii] + " ";
            }

            map.add(dica);

        }
    }

    /**
     * Pick a random word and generate underscore string
     *
     * @return
     */
    private int randomWord() {

        int randomNumber = (int) (Math.random() * map.size());
        randomNumber = ((randomNumber % 2) == 0) ? randomNumber : randomNumber - 1;
        word = map.get(randomNumber);
        wordChars = word.split("");

        tips = map.get(randomNumber + 1);

        underscores();
        return word.length();

    }

    private void underscores() {
        underscores = "";

        for (int i = 0; i < word.length(); i++) {

            if (i == word.length() - 1) {
                underscores += "_";
                return;
            }
            underscores += "_ ";
        }
    }

    private void displayLetter(boolean[] hiddenLetters, String charGuessed) {

        char[] underToChar = underscores.toCharArray();

        for (int i = 0; i < hiddenLetters.length; i++) {
            if (hiddenLetters[i]) {
                underToChar[i * 2] = charGuessed.charAt(0);
            }
        }
        underscores = String.valueOf(underToChar);
    }

    private boolean correctAnswer() {

        if (charGuessed.equals(word)) {
            p1.systemMessage("\n" + Color.GREEN_BOLD + players.get(counter).getName() + " you have won bro." + Color.RESET);
            start = false;
            return true;
        }
        return false;
    }

    private boolean alreadyTried() {

        if (!usedCharacters.add(charGuessed)) {

            p1.systemMessage(Color.RED + p1.getName() + " that character has already been tried bro." + Color.RESET);
            return true;

        }
        return false;
    }

    private boolean gameFinal() {

        if (charactersGuessed == word.length()) {
            players.get(0).systemMessage("\n" + Color.GREEN_BOLD + players.get(counter).getName() + " you have won bro." + Color.RESET);
            return true;
        } else if (tries == maxTries) {
            players.get(0).systemMessage("\n" + Color.RED_BOLD + players.get(0).getName() + " you have failed bro! iei" + Color.RESET);
            p1.systemMessage(Color.RED_BOLD + "The word was: " + word + Color.RESET);
            return true;
        }

        return false;

    }

    private void resetProperties() {
        tries = 0;
        charactersGuessed = 0;
        charactersNotGuessed = "";
        charGuessed = "";
        start = false;
        map.clear();
        usedCharacters.clear();
        underscores = "";

    }

    private void gameLogic() {

        while (charactersGuessed < word.length() && tries < maxTries) {

            try {
                charGuessed = "";
                right = false;

                if (tries == 3) {
                    p1.systemMessage("\n" + Color.YELLOW_BOLD + "hint: " + tips + Color.RESET);
                    tries++;
                }

                if (charactersNotGuessed.equals("")) {
                    p1.systemMessage(Color.CYAN + players.get(counter).getName() + " pick a character" + Color.RESET);
                  //  System.out.println(p1.getServerSocket().isBound());
                } else {
                    p1.systemMessage(Color.CYAN + players.get(counter).getName() + " pick a character. Already tried: " + charactersNotGuessed + Color.RESET);
                }

                if (players.get(counter).getRead() == null) {
                    players.remove(counter);
                    if (counter == players.size() - 1) {
                        counter = 0;
                    }
                    break;
                }

                if ((charGuessed = players.get(counter).getRead().readLine().toLowerCase()) == null) {
                    break;
                }

                p1.systemMessage(Color.YELLOW + players.get(counter).getName() + " tried " + charGuessed + "." + Color.RESET);

                if (correctAnswer()) {
                    return;
                }

                if (alreadyTried()) {

                    continue;
                }

                checkChar(charGuessed);
                if (gameFinal()){
                    return;
                }

                if (!right) {
                    if (counter < players.size() - 1) {
                        counter++;
                    } else {
                        counter = 0;
                    }
                }

            } catch (IOException e) {
            }
        }
    }
}