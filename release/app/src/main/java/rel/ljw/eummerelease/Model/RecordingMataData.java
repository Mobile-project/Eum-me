package rel.ljw.eummerelease.Model;


import java.util.List;

public class RecordingMataData {
//     * _id              INTEGER     무시하는 프라이머리 키
//     * file_name        TEXT        녹음 파일 이름
//     * memo             TEXT        메모 내용
//     * memo_time        TEXT        메모한 시간
//     * memo_index       INTEGER     몇번째 메모인지

    String fileName;
    List<memoItem> memoItemList;

    // constructor;
    public RecordingMataData(String fileName, List<memoItem> memoItemList) {
        this.fileName = fileName;
        this.memoItemList = memoItemList;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public List<memoItem> getMemoItemList() {
        return memoItemList;
    }

    public void setMemoItemList(List<memoItem> memoItemList) {
        this.memoItemList = memoItemList;
    }


}
