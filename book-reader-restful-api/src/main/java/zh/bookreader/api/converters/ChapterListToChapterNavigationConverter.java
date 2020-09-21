package zh.bookreader.api.converters;

import com.google.common.collect.ImmutableList;
import org.springframework.stereotype.Component;
import zh.bookreader.api.commands.ChapterNavigationCommand;
import zh.bookreader.model.Chapter;

import java.util.List;

@Component
public class ChapterListToChapterNavigationConverter {
    public ChapterNavigationCommand convert(String chapterId, List<Chapter> chapters) {
        ImmutableList<String> chapterIds = chapters.stream()
                .map(Chapter::getId)
                .collect(ImmutableList.toImmutableList());
        int index = chapterIds.indexOf(chapterId);

        return ChapterNavigationCommand.builder()
                .prev(getPrev(chapterIds, index))
                .next(getNext(chapterIds, index))
                .build();
    }

    private String getPrev(ImmutableList<String> chapterIds, int index) {
        return index <= 0 ? null : chapterIds.get(index - 1);
    }

    private String getNext(List<String> chapterIds, int index) {
        return index < 0 || index >= chapterIds.size() - 1 ? null : chapterIds.get(index + 1);
    }
}
