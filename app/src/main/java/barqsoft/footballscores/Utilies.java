package barqsoft.footballscores;

import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int SERIE_A = 357;
    public static final int PREMIER_LEGAUE = 354;
    public static final int CHAMPIONS_LEAGUE = 362;
    public static final int PRIMERA_DIVISION = 358;
    public static final int BUNDESLIGA = 351;
    public static final int PREMIER_LEAGUE = 398;


    public static String getLeague(int league_num)
    {
        Resources string_resourse = Resources.getSystem();
        switch (league_num)
        {
            case SERIE_A : return string_resourse.getString(R.string.seriaa);
            case PREMIER_LEGAUE : return string_resourse.getString(R.string.premierleague);
            case CHAMPIONS_LEAGUE : return string_resourse.getString(R.string.champions_league);
            case PRIMERA_DIVISION : return string_resourse.getString(R.string.primeradivison);
            case BUNDESLIGA : return string_resourse.getString(R.string.bundesliga);
            case PREMIER_LEAGUE : return  string_resourse.getString(R.string.premierleague);
            default: return string_resourse.getString(R.string.unknown_league);
        }
    }
    public static String getMatchDay(int match_day,int league_num)
    {
        Resources string_resourse = Resources.getSystem();
        if(league_num == CHAMPIONS_LEAGUE)
        {
            if (match_day <= 6)
            {
                return  string_resourse.getString(R.string.group_stage_text) + " " +
                        string_resourse.getString(R.string.matchday_text) + " :" + String.valueOf(match_day) ;
            }
            else if(match_day == 7 || match_day == 8)
            {
                return string_resourse.getString(R.string.first_knockout_round);
            }
            else if(match_day == 9 || match_day == 10)
            {
                return string_resourse.getString(R.string.quarter_final);
            }
            else if(match_day == 11 || match_day == 12)
            {
                return string_resourse.getString(R.string.semi_final);
            }
            else
            {
                return string_resourse.getString(R.string.final_text);
            }
        }
        else
        {
            return string_resourse.getString(R.string.matchday_text)+ " :" + String.valueOf(match_day);
        }
    }

    public static String getScores(int home_goals,int awaygoals)
    {
        if(home_goals < 0 || awaygoals < 0)
        {
            return " - ";
        }
        else
        {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname)
    {
        if (teamname==null){return R.drawable.no_icon;}
        switch (teamname)
        { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City FC" : return R.drawable.swansea_city_afc;
            case "Leicester City FC" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            case "Watford FC" : return R.drawable.watford_fc;
            case "Crystal Palace FC" : return R.drawable.crystal_palace_fc;
            case "West Bromwich Albion FC" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Newcastle united FC" : return R.drawable.newcastle_united;
            case "Southampton FC" : return R.drawable.southampton_fc;
            case "Chelsea FC" : return R.drawable.chelsea;
            case "Manchester City FC" : return R.drawable.manchester_city;
            case "Liverpool FC" : return R.drawable.manchester_city;
            case "AFC Bournemouth" : return R.drawable.afc_bournemouth;
            case "Aston Villa FC" : return R.drawable.aston_villa;
            case "Norwich City FC" : return R.drawable.norwich_city;
            case "Newcastle United FC" : return R.drawable.newcastle_united;
            default: return R.drawable.no_icon;
        }
    }

    /**
     * Returns true if the network is available or about to become available.
     *
     * @param c Context used to get the ConnectivityManager
     * @return true if the network is available
     */
    static public boolean isNetworkAvailable(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager)c.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
}
