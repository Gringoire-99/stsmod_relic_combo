package patch;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.relics.AbstractRelic;

import java.util.HashSet;

public class RelicComboFieldPatch {
    @SpirePatch(clz = AbstractRelic.class, method = SpirePatch.CLASS)
    public static class Fields {
        public static SpireField<HashSet<String>> relicComboTags = new SpireField<>(HashSet::new);
    }
}
