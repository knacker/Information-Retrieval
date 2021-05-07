using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace Information_Retrieval_System
{
    class Document
    {
        private int id;
        private String name;
        private String content;


        //filterlist for english words
        private List<string> englishFilter = System.IO.File.ReadLines("/res/englishST.txt").ToList();

        public Document(int id, String name, String content)
        {
            this.id = id;
            this.name = name;
            this.content = content;
        }

        public Tuple<int, String, String> getDocumentSpecs()
        {
            return Tuple.Create(id, name, content);
        }

        public void filter()
        {

        }

        public void reduce()
        {

        }

        public bool isRelevant(List<String> relevantDocs)
        {
            return true;
        }

        public void showContent()
        {
            Console.WriteLine(content);
        }
        public void showName()
        {
            Console.WriteLine(name);
        }
    }
}
