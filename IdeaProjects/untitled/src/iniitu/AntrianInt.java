public class AntrianInt {

    private static class Simpul {
        // Objek bertipe Simpul berisi item dalam bentuk
        // list berantai
        int item;
        Simpul berikut;
    }

    // Menunjuk ke simpul pertama pada antrian
    // Jika antrian kosong, maka kepala berisi null
    private Simpul kepala = null;

    private Simpul buntut = null;  // Menunjuk ke simpul akhir pada antrian

    void masuk( int N ) {
        // Menambahkan N ke akhir antrian
        Simpul buntutBaru = new Simpul();  // Simpul baru untuk menampung item baru
        buntutBaru.item = N;
        if (kepala == null) {
            // Antrian kosong, sehingga simpulBaru menjadi
            // satu-satunya simpul di dalam list. Sehingga
            // kepala dan buntut sama-sama menunjuk ke simpulBaru
            kepala = buntutBaru;
            buntut = buntutBaru;
        }
        else {
            // Simpul baru menjadi buntut antrian
            // (kepala tidak berpengaruh apa-apa)
            buntut.berikut = buntutBaru;
            buntut = buntutBaru;
        }
    }

    int keluar() {
        // Keluarkan item dari kepala antrian
        // Bisa melempar NullPointerException.
        int itemPertama = kepala.item;
        kepala = kepala.berikut;  // Sekarang item kedua menjadi kepala
        if (kepala == null) {
            // Sekarang antrian kosong. Simpul yang telah dihapus adalah
            // kepala sekaligus buntut, karena simpul ini adalah satu-satunya
            // yang ada di dalam antrian. Isi buntut dengan null.
            buntut = null;
        }
        return itemPertama;
    }

    boolean isKosong() {
        // Kembalikan true jika antrian kosong
        return (kepala == null);
    }

} // akhir kelas AntrianInt